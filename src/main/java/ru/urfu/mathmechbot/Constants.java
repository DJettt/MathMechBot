package ru.urfu.mathmechbot;

import java.util.List;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;

/**
 * <p>Ряд констант, используемый во всех состояниях MathMechBot.</p>
 */
public final class Constants {
    /**
     * <p>Приватный конструктор для класса-утилит.</p>
     */
    private Constants() {
    }

    public final static String REGISTER_COMMAND = "/register";
    public final static String INFO_COMMAND = "/info";
    public final static String EDIT_COMMAND = "/edit";
    public final static String DELETE_COMMAND = "/delete";
    public final static String ACCEPT_COMMAND = "/yes";
    public final static String DECLINE_COMMAND = "/no";
    public final static String BACK_COMMAND = "/back";

    public final static LocalButton YES_BUTTON = new LocalButton("Да", ACCEPT_COMMAND);
    public final static LocalButton NO_BUTTON = new LocalButton("Нет", DECLINE_COMMAND);
    public final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);

    public final static LocalMessage REGISTER_FIRST = new LocalMessage("Сперва нужно зарегистрироваться.");
    public final static LocalMessage ALREADY_REGISTERED = new LocalMessage(
            "Вы уже зарегистрированы. Пока что регистрировать можно только одного человека.");

    public final static LocalMessage TRY_AGAIN = new LocalMessage("Попробуйте снова.");
    public final static LocalMessage SAVED = new LocalMessage("Данные сохранены.");
    public final static LocalMessage CANCEL = new LocalMessage("Отмена...");
    public final static LocalMessage DELETED = new LocalMessage("Удаляем...");

    public final static LocalMessage HELP = new LocalMessage("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /edit - изменить информацию
            /delete - удалить информацию о Вас""");

    public final static LocalMessage FULL_NAME = new LocalMessageBuilder()
            .text("""
                    Введите свое ФИО в формате:
                    Иванов Артём Иванович
                    Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(List.of(BACK_BUTTON))
            .build();

    public final static LocalMessage YEAR = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    Constants.BACK_BUTTON))
            .build();

    public final static LocalMessage GROUP = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    Constants.BACK_BUTTON))
            .build();

    public final static LocalMessage MEN = new LocalMessageBuilder()
            .text("Введите свою академическую группу в формате:\nМЕН-123456")
            .buttons(List.of(Constants.BACK_BUTTON))
            .build();

    public final static String CONFIRM_PREFIX = "Всё верно?\n\n";

    public final static String EDITING_FULL_NAME = "full_name";
    public final static String EDITING_YEAR = "year";
    public final static String EDITING_SPECIALITY = "speciality";
    public final static String EDITING_GROUP = "number_of_group";
    public final static String EDITING_MEN = "men";

    public final static LocalMessage EDIT_CHOOSE = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME),
                    new LocalButton("Курс", EDITING_YEAR),
                    new LocalButton("Направление", EDITING_SPECIALITY),
                    new LocalButton("Группа", EDITING_GROUP),
                    new LocalButton("МЕН", EDITING_MEN),
                    Constants.BACK_BUTTON))
            .build();

    public final static LocalMessage EDIT_ADDITIONAL = new LocalMessageBuilder()
            .text("На этом всё?")
            .buttons(List.of(
                    Constants.YES_BUTTON,
                    Constants.NO_BUTTON))
            .build();
}
