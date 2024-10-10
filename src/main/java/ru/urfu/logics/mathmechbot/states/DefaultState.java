package ru.urfu.logics.mathmechbot.states;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.userstates.DeletionUserState;
import ru.urfu.logics.mathmechbot.models.userstates.RegistrationUserState;
import ru.urfu.logics.mathmechbot.states.deletion.DeletionConfirmationState;
import ru.urfu.logics.mathmechbot.states.registration.RegistrationFullNameState;


/**
 * Состояние, в котором изначально пребывает пользователь.
 */
public enum DefaultState implements MathMechBotState {
    INSTANCE;

    private final static String START_COMMAND = "/start";
    private final static String HELP_COMMAND = "/help";
    private final static String REGISTER_COMMAND = "/register";
    private final static String INFO_COMMAND = "/info";
    private final static String DELETE_COMMAND = "/delete";

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case REGISTER_COMMAND -> registerCommandHandler(context, chatId, bot);
            case INFO_COMMAND -> infoCommandHandler(context, chatId, bot);
            case DELETE_COMMAND -> deleteCommandHandler(context, chatId, bot);
            case null, default -> helpCommandHandler(context, chatId, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        final String HELP_MESSAGE = """
                %s - начало общения с ботом
                %s - выводит команды, которые принимает бот
                %s - регистрация
                %s - информация о Вас
                %s - удалить информацию о Вас"""
                .formatted(START_COMMAND, HELP_COMMAND, REGISTER_COMMAND, INFO_COMMAND, DELETE_COMMAND);
        return new LocalMessageBuilder().text(HELP_MESSAGE).build();
    }

    /**
     * Выдаёт справку.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void helpCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        bot.sendMessage(enterMessage(context, chatId), chatId);
    }

    /**
     * Запускает процесс регистрации.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void registerCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(chatId);
        if (userEntryOptional.isEmpty()) {
            context.storage.users.changeUserState(chatId, RegistrationUserState.NAME);
            bot.sendMessage(RegistrationFullNameState.INSTANCE.enterMessage(context, chatId), chatId);
        } else {
            alreadyRegistered(chatId, bot);
        }
    }

    /**
     * Выдаёт информацию о пользователе.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void infoCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(chatId);
        if (userEntryOptional.isEmpty()) {
            bot.sendMessage(Constants.ASK_FOR_REGISTRATION, chatId);
            return;
        }
        final UserEntry userEntry = userEntryOptional.get();

        final String userInfo = Constants.USER_INFO_TEMPLATE.formatted(
                String.join(" ", userEntry.surname(), userEntry.name(), userEntry.patronym()),
                userEntry.specialty(), userEntry.year(), userEntry.group(), userEntry.men());

        final LocalMessage answer = new LocalMessageBuilder()
                .text("Данные о Вас:\n\n" + userInfo)
                .build();
        bot.sendMessage(answer, chatId);
    }

    /**
     * Запускает процесс удаления.
     *
     * @param context логического ядро (контекст для состояния).
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void deleteCommandHandler(MathMechBotCore context, long chatId, Bot bot) {
        final Optional<UserEntry> userEntryOptional = context.storage.userEntries.get(chatId);
        if (userEntryOptional.isPresent()) {
            context.storage.users.changeUserState(chatId, DeletionUserState.CONFIRMATION);

            final LocalMessage msg = DeletionConfirmationState.INSTANCE.enterMessage(context, chatId);
            if (msg != null) {
                bot.sendMessage(msg, chatId);
            }
        } else {
            bot.sendMessage(Constants.ASK_FOR_REGISTRATION, chatId);
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
