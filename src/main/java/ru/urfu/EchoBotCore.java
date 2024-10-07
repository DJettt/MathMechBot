package ru.urfu;


import java.util.ArrayList;
import java.util.List;

/**
 * Логическое ядро эхо-бота.
 * Отправляет назад несколько изменённое сообщение пользователя).
 * Обрабатывает команды /help и /start, отвечая на них справкой.
 */
public class EchoBotCore extends LogicCore {
    //TODO: нужно придумать, как глобально хранить все статусы.
    private final String MESSAGE_STATUS_TEXT_ONLY = "text_only";
    private final String MESSAGE_STATUS_TEXT_WITH_BUTTONS = "text_with_buttons";
    public EchoBotCore() {}

    /**
     * Обрабатывает всю информацию, полученную с ботов.
     * @param msg сообщение, которое нужно обработать
     * @return возвращает команду ботам, что отправлять в ответ.
     */
    @Override
    public LocalMessage processMessage(LocalMessage msg) {
        if (msg.getText() != null) {
            switch (msg.getStatus()) {
                case "message" -> {
                    return switch (msg.getText()) {
                        case "/help", "/start" -> helpCommandHandler(msg);
                        case "/buttons" -> checkButtons(msg);
                    default -> defaultHandler(msg);
                };
                }
                case "callback_query"->{
                    return switch (msg.getText()){
                        case "button_1" -> new LocalMessage("Была нажата кнопка 1", MESSAGE_STATUS_TEXT_ONLY);
                        case "button_3" ->  new LocalMessage("Была нажата кнопка 3", MESSAGE_STATUS_TEXT_ONLY);
                        case "/help" -> helpCommandHandler(msg);
                    default -> helpCommandHandler(msg);
                    };
                }
                default -> {
                    System.out.println("\u001B[31m" + "UNKNOWN LocalMessage.MESSAGE_STATUS" + "\u001B[0m");
                }
            };
        }
        return null;
    }

    /**
     * @param inputMessage входящее сообщение
     * @return ответ на сообщение
     */
    private LocalMessage defaultHandler(LocalMessage inputMessage) {
        return new LocalMessage("Ты написал: " + inputMessage.getText(), MESSAGE_STATUS_TEXT_ONLY);
    }

    /**
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
        return new LocalMessage(HELP_MESSAGE, MESSAGE_STATUS_TEXT_ONLY);
    }

    /**
     * Отдельный метод для проверки работы кнопок в сообщении.
     */
    private LocalMessage checkButtons(LocalMessage inputMessage){
        List<ArrayList<LocalButton>> btns = new ArrayList<>();
        ArrayList<LocalButton> buttonList = new ArrayList<>();
        buttonList.add(new LocalButton("Кнопка номер 1", "button_1"));
        buttonList.add(new LocalButton("/help",          "/help"));
        buttonList.add(new LocalButton("Кнопка номер 3", "button_3"));
        btns.add(buttonList);
        return new LocalMessage("О, ты решил протестировать то, как работают кнопки!", MESSAGE_STATUS_TEXT_WITH_BUTTONS, btns);
    }
}
