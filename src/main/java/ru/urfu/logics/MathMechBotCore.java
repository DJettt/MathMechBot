package ru.urfu.logics;

import ru.urfu.Bot;
import ru.urfu.Message;

public class MathMechBotCore implements LogicCore {
    final static String START_COMMAND = "/start";
    final static String HELP_COMMAND = "/help";
    final static String REGISTER_COMMAND = "/register";
    final static String INFO_COMMAND = "/info";
    final static String EDIT_COMMAND = "/edit";
    final static String DELETE_COMMAND = "/delete";
    final static String BACK_COMMAND = "/back";


    @Override
    public void processMessage(Message msg, long chatId, Bot bot) {
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
    private void defaultHandler(Message inputMessage, long chatId, Bot bot) {
    }

    /**
     * Запускает процесс регистрации пользователя.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void registerCommandHandler(Message inputMessage, long chatId, Bot bot) {
    }

    /**
     * Отправляет зарегистрированную информацию.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void infoCommandHandler(Message inputMessage, long chatId, Bot bot) {
    }

    /**
     * Запускает процесс редактирования зарегистрированной информации.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void editCommandHandler(Message inputMessage, long chatId, Bot bot) {
    }

    /**
     * Запускает процесс удаления зарегистрированной информации.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void deleteCommandHandler(Message inputMessage, long chatId, Bot bot) {
    }

    /**
     * Возвращает пользователя на предыдущий шаг в запущенном процессе.
     * @param inputMessage входящее сообщение
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void backCommandHandler(Message inputMessage, long chatId, Bot bot) {
    }

    /**
     * Выдаёт справку.
     * @param inputMessage входящее сообщение с командой /help
     * @param chatId идентификатор чата отправителя
     * @param bot бот, от которого пришло сообщение
     */
    private void helpCommandHandler(Message inputMessage, Long chatId, Bot bot) {
        final String HELP_MESSAGE = "Справка";
        final Message answer = new Message(HELP_MESSAGE);
        bot.sendMessage(answer, chatId);
    }
}