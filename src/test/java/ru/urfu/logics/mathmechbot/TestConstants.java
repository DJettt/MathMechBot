package ru.urfu.logics.mathmechbot;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;

/**
 * Константы для теста матмех-бота.
 */
public final class TestConstants {
    /**
     * Приватный конструктор для класса-утилит.
     */
    private TestConstants() {
    }

    public final static String ACCEPT_COMMAND = "/yes";
    public final static LocalMessage ACCEPT_MESSAGE = new LocalMessageBuilder().text(ACCEPT_COMMAND).build();

    public final static String DECLINE_COMMAND = "/no";
    public final static LocalMessage DECLINE_MESSAGE = new LocalMessageBuilder().text(DECLINE_COMMAND).build();

    public final static String BACK_COMMAND = "/back";
    public final static LocalMessage BACK_MESSAGE = new LocalMessageBuilder().text(BACK_COMMAND).build();
    public final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);

    public final static List<LocalButton> YES_NO_BACK = new ArrayList<>(List.of(
            new LocalButton("Да", ACCEPT_COMMAND),
            new LocalButton("Неа", DECLINE_COMMAND),
            BACK_BUTTON
    ));

    public final static String INFO_COMMAND = "/info";
    public final static LocalMessage INFO_MESSAGE = new LocalMessageBuilder().text(INFO_COMMAND).build();

    public final static String DELETE_COMMAND = "/delete";
    public final static LocalMessage DELETE_MESSAGE = new LocalMessageBuilder().text(DELETE_COMMAND).build();

    public final static String REGISTER_COMMAND = "/register";
    public final static LocalMessage REGISTER_MESSAGE = new LocalMessageBuilder().text(REGISTER_COMMAND).build();

    public final static LocalMessage HELP = new LocalMessageBuilder()
            .text("""
                    /start - начало общения с ботом
                    /help - выводит команды, которые принимает бот
                    /register - регистрация
                    /info - информация о Вас
                    /edit - изменить информацию
                    /delete - удалить информацию о Вас""")
            .build();
    public final static LocalMessage ASK_FOR_REGISTRATION = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();
    public final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.").build();

    public final static String EDIT_COMMAND = "/edit";
    public final static LocalMessage EDIT_MESSAGE = new LocalMessageBuilder().text(EDIT_COMMAND).build();

    public final static String EDITING_FULL_NAME_COMMAND = "full_name";
    public final static String EDITING_YEAR_COMMAND = "year";
    public final static String EDITING_SPECIALITY_COMMAND = "speciality";
    public final static String EDITING_GROUP_COMMAND = "number_of_group";
    public final static String EDITING_MEN_COMMAND = "men";
    public final static LocalMessage EDITING_CHOOSE_MESSAGE = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME_COMMAND),
                    new LocalButton("Курс", EDITING_YEAR_COMMAND),
                    new LocalButton("Направление", EDITING_SPECIALITY_COMMAND),
                    new LocalButton("Группа", EDITING_GROUP_COMMAND),
                    new LocalButton("МЕН", EDITING_MEN_COMMAND),
                    Constants.BACK_BUTTON)))
            .build();
    public final static LocalMessage EDITING_GROUP_MESSAGE = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(new ArrayList<>(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    Constants.BACK_BUTTON)))
            .build();
}
