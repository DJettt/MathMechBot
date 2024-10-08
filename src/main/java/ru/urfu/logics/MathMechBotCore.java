package ru.urfu.logics;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.urfu.bots.Bot;
import ru.urfu.enums.processes.DeletionProcessState;
import ru.urfu.enums.processes.Process;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.models.User;
import ru.urfu.models.UserEntry;
import ru.urfu.storages.ArrayStorage;
import ru.urfu.storages.Storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Логическое ядро бота, парсящего каналы в Telegram на предмет упоминания студентов.
 * На данный момент просто сохраняет информацию о тех пользователях, чьи упоминания надо искать.
 */
public class MathMechBotCore implements LogicCore {
    final static String START_COMMAND = "/start";
    final static String HELP_COMMAND = "/help";
    final static String REGISTER_COMMAND = "/register";
    final static String INFO_COMMAND = "/info";
    final static String EDIT_COMMAND = "/edit";
    final static String DELETE_COMMAND = "/delete";
    final static String BACK_COMMAND = "/back";

    final static String USER_INFO_TEMPLATE = """
            ФИО: %s %s %s
            Направление: %s
            Курс: %d
            Группа: %d""";

    final Storage<User, Long> users;
    final Storage<UserEntry, Long> userEntries;

    /**
     * Конструктор.
     */
    public MathMechBotCore() {
        users = new ArrayStorage<>();
        userEntries = new ArrayStorage<>();
    }


    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        if (msg.text() == null) {
            return;
        }

        switch (msg.text()) {
            case START_COMMAND, HELP_COMMAND -> helpCommandHandler(msg, chatId, bot);
            case REGISTER_COMMAND -> registerCommandHandler(msg, chatId, bot);
            case INFO_COMMAND -> infoCommandHandler(msg, chatId, bot);
            case EDIT_COMMAND -> editCommandHandler(msg, chatId, bot);
            case DELETE_COMMAND -> deleteCommandHandler(msg, chatId, bot);
            case BACK_COMMAND -> backCommandHandler(msg, chatId, bot);
            default -> defaultHandler(msg, chatId, bot);
        }
    }

    /**
     * Обрабатывает сообщения, не распознанные как заявленные команды.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void defaultHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        final User user = users.getById(chatId);
        if (user == null) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Сперва зарегистрируйтесь с помощью команды /register")
                    .build(), chatId);
            return;
        }

        switch (user.currentProcess()) {
            case Process.REGISTRATION -> registerCommandHandler(inputMessage, chatId, bot);
            case Process.DELETION -> deleteCommandHandler(inputMessage, chatId, bot);
            case Process.EDITION -> editCommandHandler(inputMessage, chatId, bot);
            default -> helpCommandHandler(inputMessage, chatId, bot);
        }
    }

    /**
     * Запускает процесс регистрации пользователя.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void registerCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        if (users.getById(chatId) == null) {
            users.add(new User(chatId, chatId, chatId, null, null));
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Зарегал тебя, твой id " + chatId)
                    .build(), chatId);
        }
    }

    /**
     * Отправляет зарегистрированную информацию.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void infoCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        LocalMessage msg;
        if (users.getById(chatId).getCurrentProcess() == null) {
            String userInfo = String.format(USER_INFO_TEMPLATE,
                    userEntries.getById(chatId).surname(),
                    userEntries.getById(chatId).name(),
                    userEntries.getById(chatId).patronym(),
                    userEntries.getById(chatId).specialty(),
                    userEntries.getById(chatId).year(),
                    userEntries.getById(chatId).group()
            );
            msg = new LocalMessage(userInfo, null);
        } else {
            msg = new LocalMessage("Сейчас просмотр информации невозможен.\n" +
                    "Повторите попытку после завершения текущего действия",
                    null);
        }
        bot.sendMessage(msg, chatId);
    }

    /**
     * Запускает процесс редактирования зарегистрированной информации.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void editCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
    }

    /**
     * Запускает процесс удаления зарегистрированной информации.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void deleteCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        final User user = users.getById(chatId);

        if (user == null) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Сперва зарегистрируйтесь с помощью команды /register")
                    .build(), chatId);
            return;
        }

        if (user.currentProcess() == null) {
            user.setCurrentProcess(Process.DELETION);
            user.setCurrentState(DeletionProcessState.CONFIRMATION);
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Точно удалить?")
                    .buttons(new ArrayList<>(
                            List.of(
                                    new LocalButton("да", "да"),
                                    new LocalButton("нет", "нет")
                            )))
                    .build(), chatId);
            return;
        } else if (user.currentProcess() != Process.DELETION) {
            bot.sendMessage(new LocalMessageBuilder()
                    .text("Вы находитесь посреди другого процесса, завершите его.")
                    .build(), chatId);
            return;
        }

        final LocalMessageBuilder responseBuilder = new LocalMessageBuilder();
        if (user.currentState() == DeletionProcessState.CONFIRMATION) {
            if (Objects.equals(inputMessage.text(), "да")) {
                // TODO: удаляй нормально
                users.deleteById(chatId);
                userEntries.deleteById(chatId);
                user.setCurrentProcess(null);
                user.setCurrentState(null);
                responseBuilder.text("Удаляю...");
            } else if (Objects.equals(inputMessage.text(), "нет")) {
                user.setCurrentProcess(null);
                user.setCurrentState(null);
                responseBuilder.text("Не удаляю...");
            } else {
                responseBuilder.text("Не понял, повтори.");
            }
        }
        bot.sendMessage(responseBuilder.build(), chatId);
    }

    /**
     * Возвращает пользователя на предыдущий шаг в запущенном процессе.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void backCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
    }

    /**
     * Выдаёт справку.
     * @param inputMessage входящее сообщение с командой /help
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void helpCommandHandler(LocalMessage inputMessage, Long chatId, Bot bot) {
        final String HELP_MESSAGE = "Справка";
        final LocalMessage answer = new LocalMessage(HELP_MESSAGE, null);
        bot.sendMessage(answer, chatId);
    }
}
