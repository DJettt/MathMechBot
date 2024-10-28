package ru.urfu.logics.mathmechbot.states;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
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

    private final LocalMessage askForRegistration = new LocalMessage("Сперва нужно зарегистрироваться.");

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        switch (message.text()) {
            case REGISTER_COMMAND -> registerCommandHandler(contextCore, chatId, message, bot);
            case INFO_COMMAND -> infoCommandHandler(contextCore, chatId, bot);
            case DELETE_COMMAND -> deleteCommandHandler(contextCore, chatId, message, bot);
            case EDIT_COMMAND -> editCommandHandler(contextCore, chatId, message, bot);
            case null, default -> helpCommandHandler(contextCore, chatId, message, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        final String HELP_MESSAGE = """
                %s - начало общения с ботом
                %s - выводит команды, которые принимает бот
                %s - регистрация
                %s - информация о Вас
                %s - изменить информацию
                %s - удалить информацию о Вас"""
                .formatted(START_COMMAND, HELP_COMMAND, REGISTER_COMMAND, INFO_COMMAND, EDIT_COMMAND, DELETE_COMMAND);
        return new LocalMessage(HELP_MESSAGE);
    }

    /**
     * Выдаёт справку.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void helpCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        bot.sendMessage(enterMessage(contextCore, chatId, message, bot),  chatId);
    }

    /**
     * Запускает процесс регистрации.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void registerCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                        @NotNull LocalMessage message, @NotNull Bot bot) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);
        if (userEntryOptional.isEmpty()) {
            contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.REGISTRATION_NAME);
            bot.sendMessage(new RegistrationFullNameState().enterMessage(contextCore, chatId, message, bot),  chatId);
        } else {
            alreadyRegistered(chatId, bot);
        }
    }

    /**
     * Выдаёт информацию о пользователе.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param bot бот в котором пришло сообщение
     */
    public void infoCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                   @NotNull Bot bot) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);
        if (userEntryOptional.isEmpty()) {
            bot.sendMessage(askForRegistration,  chatId);
            return;
        }
        final LocalMessage answer = new LocalMessage("Данные о Вас:\n\n"
                + userEntryOptional.get().toHumanReadable());
        bot.sendMessage(answer,  chatId);
    }

    /**
     * Запускает процесс изменения информации о пользователе.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void editCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);
        if (userEntryOptional.isPresent()) {
            contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.EDITING_CHOOSE);

            final LocalMessage msg = new EditingChooseState().enterMessage(contextCore, chatId, message, bot);
            bot.sendMessage(msg,  chatId);
        } else {
            bot.sendMessage(askForRegistration,  chatId);
        }
    }

    /**
     * Запускает процесс удаления.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void deleteCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                      @NotNull LocalMessage message, @NotNull Bot bot) {
        final Optional<UserEntry> userEntryOptional = contextCore.getStorage().getUserEntries().get(chatId);
        if (userEntryOptional.isPresent()) {
            contextCore.getStorage().getUsers().changeUserState(chatId,
                    MathMechBotUserState.DELETION_CONFIRMATION);

            final LocalMessage msg = new DeletionConfirmationState().enterMessage(contextCore, chatId, message, bot);
            if (msg != null) {
                bot.sendMessage(msg,  chatId);
            }
        } else {
            bot.sendMessage(askForRegistration,  chatId);
        }
    }

    /**
     * Говорит пользователю, что тот уже зарегистрировался.
     *
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void alreadyRegistered(long chatId, Bot bot) {
        final LocalMessage answer =
                new LocalMessage("Вы уже зарегистрированы. Пока что регистрировать можно только одного человека.");
        bot.sendMessage(answer, chatId);
    }
}
