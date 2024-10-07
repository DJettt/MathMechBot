package ru.urfu;


import java.util.ArrayList;
import java.util.List;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя).
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore extends LogicCore {
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
                case "/help", "/start" -> helpCommandHandler(msg);
                case "/buttons" -> buttonsCommandHandler(msg);
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
     * @param inputMessage входящее сообщение с командой /help
     * @return ответ на сообщение (содержит справку)
     */
    private LocalMessage helpCommandHandler(LocalMessage inputMessage) {
        String HELP_MESSAGE = """
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
     */
    private LocalMessage buttonsCommandHandler(LocalMessage inputMessage) {
        List<List<LocalButton>> buttonGrid = new ArrayList<>();
        List<LocalButton> buttonRow = new ArrayList<>();

        buttonRow.add(new LocalButton("Кнопка номер 1", "button_1"));
        buttonRow.add(new LocalButton("/help",          "/help"));
        buttonRow.add(new LocalButton("Кнопка номер 3", "button_3"));

        buttonGrid.add(buttonRow);
        return new LocalMessage("О, ты решил протестировать то, как работают кнопки!", buttonGrid);
    }
}
