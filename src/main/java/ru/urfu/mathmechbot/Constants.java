package ru.urfu.mathmechbot;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;

/**
 * Ряд констант, используемый во всех состояниях MathMechBot.
 */
public final class Constants {
    /**
     * Приватный конструктор для класса-утилит.
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
    public final static LocalButton NO_BUTTON = new LocalButton("Неа", DECLINE_COMMAND);
    public final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);

    public final static LocalMessage REGISTER_FIRST = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.")
            .build();
    public final static LocalMessage ALREADY_REGISTERED = new LocalMessageBuilder()
            .text("Вы уже зарегистрированы. Пока что регистрировать можно только одного человека.")
            .build();

    public final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.")
            .build();
    public final static LocalMessage SAVED = new LocalMessageBuilder()
            .text("Данные сохранены.")
            .build();
    public final static LocalMessage CANCEL = new LocalMessageBuilder()
            .text("Отмена...")
            .build();
    public final static LocalMessage DELETED = new LocalMessageBuilder()
            .text("Удаляем...")
            .build();

    public final static LocalMessage HELP = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /edit - изменить информацию
                    /delete - удалить информацию о Вас""")
            .build();
    public final static LocalMessage FULL_NAME = new LocalMessageBuilder()
            .text("""
                    Введите свое ФИО в формате:
                    Иванов Артём Иванович
                    Без дополнительных пробелов и с буквой ё, если нужно.""")
            .buttons(new ArrayList<>(List.of(BACK_BUTTON)))
            .build();
    public final static LocalMessage YEAR = new LocalMessageBuilder()
            .text("На каком курсе Вы обучаетесь?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 курс", "1"),
                    new LocalButton("2 курс", "2"),
                    new LocalButton("3 курс", "3"),
                    new LocalButton("4 курс", "4"),
                    new LocalButton("5 курс", "5"),
                    new LocalButton("6 курс", "6"),
                    Constants.BACK_BUTTON
            )))
            .build();
    public final static LocalMessage GROUP = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    Constants.BACK_BUTTON)))
            .build();
    public final static LocalMessage MEN = new LocalMessageBuilder()
            .text("Введите свою академическую группу в формате:\nМЕН-123456")
            .buttons(new ArrayList<>(List.of(Constants.BACK_BUTTON)))
            .build();
    public final static String CONFIRM_PREFIX = "Всё верно?\n\n";

    public final static String EDITING_FULL_NAME = "full_name";
    public final static String EDITING_YEAR = "year";
    public final static String EDITING_SPECIALITY = "speciality";
    public final static String EDITING_GROUP = "number_of_group";
    public final static String EDITING_MEN = "men";

    public final static LocalMessage EDIT_CHOOSE = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME),
                    new LocalButton("Курс", EDITING_YEAR),
                    new LocalButton("Направление", EDITING_SPECIALITY),
                    new LocalButton("Группа", EDITING_GROUP),
                    new LocalButton("МЕН", EDITING_MEN),
                    Constants.BACK_BUTTON)))
            .build();
    public final static LocalMessage EDIT_ADDITIONAL = new LocalMessageBuilder()
            .text("На этом всё?")
            .buttons(new ArrayList<>(List.of(
                    Constants.YES_BUTTON,
                    Constants.NO_BUTTON)))
            .build();
}
