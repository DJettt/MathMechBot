package ru.urfu.logics.mathmechbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.enums.DeletionStateList;
import ru.urfu.logics.mathmechbot.enums.RegistrationStateList;
import ru.urfu.logics.mathmechbot.models.UserEntry;


/**
 * Состояние, в котором изначально пребывает пользователь.
 */
public class DefaultState extends MathMechBotState {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultState.class);

    private final static String START_COMMAND = "/start";
    private final static String HELP_COMMAND = "/help";
    private final static String REGISTER_COMMAND = "/register";
    private final static String INFO_COMMAND = "/info";
    private final static String DELETE_COMMAND = "/delete";

    /**
     * Конструктор состояния.
     *
     * @param context контекст (в том же смысле, что и в паттерне "State").
     */
    public DefaultState(MathMechBotCore context) {
        super(context);
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case REGISTER_COMMAND -> registerCommandHandler(msg, chatId, bot);
            case INFO_COMMAND -> infoCommandHandler(msg, chatId, bot);
            case DELETE_COMMAND -> deleteCommandHandler(msg, chatId, bot);
            case null, default -> helpCommandHandler(msg, chatId, bot);
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        helpCommandHandler(msg, chatId, bot);
    }

    /**
     * Выдаёт справку.
     *
     * @param message входящее сообщение
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void helpCommandHandler(LocalMessage message, long chatId, Bot bot) {
        final String HELP_MESSAGE = """
                %s - начало общения с ботом
                %s - выводит команды, которые принимает бот
                %s - регистрация
                %s - информация о Вас
                %s - удалить информацию о Вас"""
                .formatted(START_COMMAND, HELP_COMMAND, REGISTER_COMMAND, INFO_COMMAND, DELETE_COMMAND);
        final LocalMessage answer = new LocalMessageBuilder().text(HELP_MESSAGE).build();
        bot.sendMessage(answer, chatId);
    }

    /**
     * Запускает процесс регистрации.
     *
     * @param message входящее сообщение
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void registerCommandHandler(LocalMessage message, long chatId, Bot bot) {
        final UserEntry userEntry = context.userEntries.getById(chatId);
        if (userEntry == null) {
            context.users.changeUserState(chatId, RegistrationStateList.NAME);
            new RegistrationFullNameState(context).onEnter(message, chatId, bot);
        } else {
            alreadyRegistered(message, chatId, bot);
        }
    }

    /**
     * Выдаёт информацию о пользователе.
     * @param message входящее сообщение
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void infoCommandHandler(LocalMessage message, long chatId, Bot bot) {
        final UserEntry userEntry = context.userEntries.getById(chatId);
        if (userEntry == null) {
            bot.sendMessage(ASK_FOR_REGISTRATION, chatId);
            return;
        }

        final String userInfo = USER_INFO_TEMPLATE.formatted(
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
     * @param message входящее сообщение
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void deleteCommandHandler(LocalMessage message, long chatId, Bot bot) {
        final UserEntry userEntry = context.userEntries.getById(chatId);
        if (userEntry != null) {
            context.users.changeUserState(chatId, DeletionStateList.CONFIRMATION);
            new DeletionConfirmationState(context).onEnter(message, chatId, bot);
        } else {
            bot.sendMessage(ASK_FOR_REGISTRATION, chatId);
        }
    }

    /**
     * Говорит пользователю, что тот уже зарегистрировался.
     *
     * @param message входящее сообщение
     * @param chatId  идентификатор чата отправителя
     * @param bot     бот, от которого пришло сообщение
     */
    private void alreadyRegistered(LocalMessage message, long chatId, Bot bot) {
        final LocalMessage answer = new LocalMessageBuilder()
                .text("Вы уже зарегистрированы. Пока что регистрировать можно только одного человека.")
                .build();
        bot.sendMessage(answer, chatId);
    }
}
