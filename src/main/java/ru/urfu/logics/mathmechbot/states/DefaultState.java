package ru.urfu.logics.mathmechbot.states;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.states.deletion.DeletionConfirmationState;
import ru.urfu.logics.mathmechbot.states.editing.EditingChooseState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationFullNameState;


/**
 * Состояние, в котором изначально пребывает пользователь.
 */
public final class DefaultState implements MathMechBotState {
    private final static String START_COMMAND = "/start";
    private final static String HELP_COMMAND = "/help";
    private final static String REGISTER_COMMAND = "/register";
    private final static String INFO_COMMAND = "/info";
    private final static String EDIT_COMMAND = "/edit";
    private final static String DELETE_COMMAND = "/delete";

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        switch (request.message().text()) {
            case REGISTER_COMMAND -> registerCommandHandler(context, request);
            case INFO_COMMAND -> infoCommandHandler(context, request);
            case DELETE_COMMAND -> deleteCommandHandler(context, request);
            case EDIT_COMMAND -> editCommandHandler(context, request);
            case null, default -> helpCommandHandler(context, request);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, @NotNull Request request) {
        final String HELP_MESSAGE = """
                %s - начало общения с ботом
                %s - выводит команды, которые принимает бот
                %s - регистрация
                %s - информация о Вас
                %s - изменить информацию
                %s - удалить информацию о Вас"""
                .formatted(START_COMMAND, HELP_COMMAND, REGISTER_COMMAND, INFO_COMMAND, EDIT_COMMAND, DELETE_COMMAND);
        return new LocalMessageBuilder().text(HELP_MESSAGE).build();
    }

    /**
     * Выдаёт справку.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void helpCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        request.bot().sendMessage(enterMessage(context, request), request.id());
    }

    /**
     * Запускает процесс регистрации.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void registerCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());
        if (userEntryOptional.isEmpty()) {
            context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.REGISTRATION_NAME);
            request.bot().sendMessage(new RegistrationFullNameState().enterMessage(context, request), request.id());
        } else {
            alreadyRegistered(request.id(), request.bot());
        }
    }

    /**
     * Выдаёт информацию о пользователе.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    public void infoCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());
        if (userEntryOptional.isEmpty()) {
            request.bot().sendMessage(new Constants().askForRegistration, request.id());
            return;
        }
        final LocalMessage answer = new LocalMessageBuilder()
                .text("Данные о Вас:\n\n" + userEntryOptional.get().toHumanReadable())
                .build();
        request.bot().sendMessage(answer, request.id());
    }

    /**
     * Запускает процесс изменения информации о пользователе.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void editCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());
        if (userEntryOptional.isPresent()) {
            context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.EDITING_CHOOSE);

            final LocalMessage msg = new EditingChooseState().enterMessage(context, request);
            request.bot().sendMessage(msg, request.id());
        } else {
            request.bot().sendMessage(new Constants().askForRegistration, request.id());
        }
    }

    /**
     * Запускает процесс удаления.
     *
     * @param context логического ядро (контекст для состояния).
     * @param request запрос.
     */
    private void deleteCommandHandler(@NotNull MathMechBotCore context, @NotNull Request request) {
        final Optional<UserEntry> userEntryOptional = context.getStorage().getUserEntries().get(request.id());
        if (userEntryOptional.isPresent()) {
            context.getStorage().getUsers().changeUserState(request.id(), MathMechBotUserState.DELETION_CONFIRMATION);

            final LocalMessage msg = new DeletionConfirmationState().enterMessage(context, request);
            if (msg != null) {
                request.bot().sendMessage(msg, request.id());
            }
        } else {
            request.bot().sendMessage(new Constants().askForRegistration, request.id());
        }
    }

    /**
     * Говорит пользователю, что тот уже зарегистрировался.
     *
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void alreadyRegistered(long chatId, Bot bot) {
        final LocalMessage answer = new LocalMessageBuilder()
                .text("Вы уже зарегистрированы. Пока что регистрировать можно только одного человека.")
                .build();
        bot.sendMessage(answer, chatId);
    }
}
