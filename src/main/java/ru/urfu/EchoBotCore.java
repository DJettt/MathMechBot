package ru.urfu;


/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя).
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore extends LogicCore {

    public EchoBotCore() {}

    @Override
    public LocalMessage processMessage(LocalMessage msg) {
        if (msg.getText() != null) {
            return switch (msg.getText()) {
                case "/help", "/start" -> helpCommandHandler(msg);
                default -> defaultHandler(msg);
            };
        }

        return null;
    }

    /**
     * @param inputMessage входящее сообщение
     * @return ответ на сообщение
     */
    private LocalMessage defaultHandler(LocalMessage inputMessage) {
        return new LocalMessage("Ты написал: " + inputMessage.getText());
    }

    /**
     * @param inputMessage входящее сообщение с командой /help
     * @return ответ на сообщение (содержит справку)
     */
    private LocalMessage helpCommandHandler(LocalMessage inputMessage) {
        String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";
        return new LocalMessage(HELP_MESSAGE);
    }
}
