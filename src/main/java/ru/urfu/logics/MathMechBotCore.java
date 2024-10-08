package ru.urfu.logics;

import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
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
        switch (users.getById(chatId).getCurrentProcess()){
            case "process_delete" -> deleteCommandHandler(inputMessage, chatId, bot);
            case "process_edit" -> editCommandHandler(inputMessage, chatId, bot);
            case "process_register" -> registerCommandHandler(inputMessage, chatId, bot);
            case "process_info" -> infoCommandHandler(inputMessage, chatId, bot);
            default -> {
                LocalMessage msg = new LocalMessage("Извините, произошла непредвиденная ошибка.",
                        null);
                bot.sendMessage(msg, chatId);
            }
        }
    }

    /**
     * Запускает процесс регистрации пользователя.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void registerCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {
        User user = new User(chatId, chatId, "chatid", null, 0);
        users.add(user);
        LocalMessage msg = new LocalMessage("Вы были успешно зарегистрированы.", null);
        bot.sendMessage(msg, chatId);
    }

    /**
     * Отправляет зарегистрированную информацию.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void infoCommandHandler(LocalMessage inputMessage, long chatId, Bot bot) {

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
        if (users.getById(chatId) != null) {
            if (users.getById(chatId).currentProcess() == null) {
                users.getById(chatId).setCurrentProcess("process_delete");
                users.getById(chatId).setCurrentState(0);
                List<LocalButton> buttons = List
                        .of(new LocalButton("Да", "delete_sure_yes"),
                                new LocalButton("Нет", "delete_sure_no"));
                LocalMessage msg = new LocalMessage("Вы уверены, что хотите удалить все данные?", buttons);
                bot.sendMessage(msg, chatId);
            } else if (Objects.equals(users.getById(chatId).currentProcess(), "process_deleting")) {
                switch (inputMessage.text()){
                    case "delete_sure_yes" -> users.getById(chatId).setCurrentState(1);
                    case "delete_sure_no" -> users.getById(chatId).setCurrentState(2);
                    default -> users.getById(chatId).setCurrentState(1000);
                }
                int currentPosition = users.getById(chatId).getCurrentState();
                LocalMessage msg = null;
                switch (currentPosition) {
                    case 0 -> {
                        List<LocalButton> buttons = List
                                .of(new LocalButton("Да", "delete_sure_yes"),
                                        new LocalButton("Нет", "delete_sure_no"));
                        msg = new LocalMessage("Вы уверены, что хотите удалить все данные?", buttons);
                    }
                    case 1 -> {
                        users.removeById(chatId);
                        msg = new LocalMessage("Удаление завершено.", null);
                    }
                    case 2 -> {
                        msg = new LocalMessage("Удаление отменено.", null);
                        users.getById(chatId).setCurrentState(0);
                        users.getById(chatId).setCurrentProcess(null);
                    }
                    default -> {
                        msg = new LocalMessage("Что-то пошло не так...", null);
                        System.out.println("ОШИБКА!!!");
                    }
                }
                bot.sendMessage(msg, chatId);
            }
        } else {
            LocalMessage msg = new LocalMessage("Данный пользователь не зарегистрирован.", null);
            bot.sendMessage(msg, chatId);
        }
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
