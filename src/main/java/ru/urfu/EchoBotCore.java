package ru.urfu;


/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя).
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore extends LogicCore {
    @Override
    public Message processMessage(Message msg) {
        if (msg.getText() != null) {
            return switch (msg.getText()) {
                case "/help", "/start" -> helpCommandHandler(msg);
                default -> defaultHandler(msg);
            };
        }

        return null;
    }

    /**
     * Обрабатывает сообщения, не распознанные как заявленные команды.
     * @param inputMessage входящее сообщение
     * @return ответ на сообщение
     */
    private Message defaultHandler(Message inputMessage) {
        return new Message("Ты написал: " + inputMessage.getText());
    }

    /**
     * Выдаёт справку.
     * @param inputMessage входящее сообщение с командой /help
     * @return ответ на сообщение (содержит справку)
     */
    private Message helpCommandHandler(Message inputMessage) {
        final String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";
        return new Message(HELP_MESSAGE);
    }
}
