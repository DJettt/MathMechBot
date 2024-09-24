package ru.urfu;

public class EchoBotCore extends LogicCore {

    public EchoBotCore() {}

    public Message processMessage(Message msg) {
        if (msg.getText() != null) {
            return switch (msg.getText()) {
                case "/help", "/start" -> helpCommandHandler(msg);
                default -> defaultHandler(msg);
            };
        }

        return null;
    }

    private Message defaultHandler(Message inputMeassage) {
        return new Message("Ты написал: " + inputMeassage.getText());
    }

    private Message helpCommandHandler(Message inputMessage) {
        String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";
        return new Message(HELP_MESSAGE);
    }
}
