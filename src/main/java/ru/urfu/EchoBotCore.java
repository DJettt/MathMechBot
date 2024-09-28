package ru.urfu;


import java.util.ArrayList;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя).
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore extends LogicCore {

    public EchoBotCore() {}

    @Override
    public Message processMessage(Message msg, Long chatId, Bot bot) {
        if (msg.text() == null) {
            if (!msg.images().isEmpty()) {
                return defaultHandler(msg, chatId, bot);
            }
            return null;
        }

        return switch (msg.text()) {
            case "/help", "/start" -> helpCommandHandler(msg, chatId, bot);
            default -> defaultHandler(msg, chatId, bot);
        };
    }


    /**
     * @param inputMessage входящее сообщение
     * @return ответ на сообщение
     */
    private Message defaultHandler(Message inputMessage, Long chatId, Bot bot) {
        final Message answer = new Message(
                (inputMessage.text() != null) ? ("Ты написал: " + inputMessage.text()) : null,
                inputMessage.images());
        bot.sendMessage(answer, chatId);
        return answer;
    }


    /**
     * @param inputMessage входящее сообщение с командой /help
     * @return ответ на сообщение (содержит справку)
     */
    private Message helpCommandHandler(Message inputMessage, Long chatId, Bot bot) {
        String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";

        final Message answer = new Message(HELP_MESSAGE, new ArrayList<>());
        bot.sendMessage(answer, chatId);
        return answer;
    }
}
