package ru.urfu;

import java.util.ArrayList;
import java.util.List;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя.
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore extends LogicCore {

    /**
     * Пустой конструктор.
     */
    public EchoBotCore() {}

    /**
     * Обрабатывает всю информацию, полученную с ботов.
     * @param msg сообщение, которое нужно обработать
     * @return возвращает команду ботам, что отправлять в ответ.
     */
    @Override
    public LocalMessage processMessage(LocalMessage msg) {
        if (msg.getText() != null) {
            return switch (msg.getText()) {
                case "/help", "/start" -> helpCommandHandler();
                case "/buttons" -> buttonsCommandHandler();
                case "button_1" -> new LocalMessage("Была нажата кнопка 1");
                case "button_3" ->  new LocalMessage("Была нажата кнопка 3");
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
    private LocalMessage defaultHandler(LocalMessage inputMessage) {
        return new LocalMessage("Ты написал: " + inputMessage.getText());
    }

    /**
     * Выдаёт справку.
     * @return ответ на сообщение (содержит справку)
     */
    @SuppressWarnings("LineLength")
    private LocalMessage helpCommandHandler() {
        final String HELP_MESSAGE = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.\n
                Пассивная способность: Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!\n
                /help - Показать доступные команды.
                /start - Начинает диалог с начала. (нет)
                /buttons - на случай если хочется поиграться с кнопками.\n
                Приятного использования!""";
        return new LocalMessage(HELP_MESSAGE);
    }

    /**
     * Отдельный метод для проверки работы кнопок в сообщении.
     * @return возвращает сообщение для отправки пользователю.
     */
    private LocalMessage buttonsCommandHandler() {
        List<LocalButton> buttonRow = new ArrayList<>();

        buttonRow.add(new LocalButton("1", "button_1"));
        buttonRow.add(new LocalButton("/help",          "/help"));
        buttonRow.add(new LocalButton("3", "button_3"));
        buttonRow.add(new LocalButton("4", "button_4"));
        buttonRow.add(new LocalButton("5", "button_5"));
        buttonRow.add(new LocalButton("6", "button_6"));
        buttonRow.add(new LocalButton("7", "button_7"));
        buttonRow.add(new LocalButton("8", "button_8"));
        buttonRow.add(new LocalButton("9", "button_9"));
        buttonRow.add(new LocalButton("10", "button_10"));
        buttonRow.add(new LocalButton("11", "button_11"));

        return new LocalMessage("О, ты решил протестировать то, как работают кнопки!", buttonRow);
    }
}
