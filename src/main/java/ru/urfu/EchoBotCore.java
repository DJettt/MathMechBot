package ru.urfu;


import java.util.ArrayList;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя).
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore implements LogicCore {
    final static String START_COMMAND = "/start";
    final static String HELP_COMMAND = "/help";


    @Override
    public void processMessage(Message msg, Long chatId, Bot bot) {
        // Нет текста, но есть картинки.
        if (msg.text() == null) {
            if (!msg.images().isEmpty()) {
                defaultHandler(msg, chatId, bot);
            }
            return;
        }

        // Есть текст.
        switch (msg.text()) {
            case START_COMMAND, HELP_COMMAND -> helpCommandHandler(msg, chatId, bot);
            default -> defaultHandler(msg, chatId, bot);
        }
    }


    /**
     * @param inputMessage входящее сообщение
     */
    private void defaultHandler(Message inputMessage, Long chatId, Bot bot) {
        final Message answer = new Message(
                (inputMessage.text() != null) ? ("Ты написал: " + inputMessage.text()) : null,
                inputMessage.images());
        bot.sendMessage(answer, chatId);
    }


    /**
     * @param inputMessage входящее сообщение с командой /help
     */
    private void helpCommandHandler(Message inputMessage, Long chatId, Bot bot) {
        String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";

        final Message answer = new Message(HELP_MESSAGE, new ArrayList<>());
        bot.sendMessage(answer, chatId);
    }
}
