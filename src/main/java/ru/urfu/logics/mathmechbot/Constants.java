package ru.urfu.logics.mathmechbot;

import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;

/**
 * Ряд констант, используемый во всех состояниях MathMechBot.
 */
public final class Constants {
    /**
     * Приватный конструктор для класса-утилит.
     */
    private Constants() {
    }

    public final static String ACCEPT_COMMAND = "/yes";
    public final static String DECLINE_COMMAND = "/no";
    public final static String BACK_COMMAND = "/back";

    public final static LocalButton YES_BUTTON = new LocalButton("Да", ACCEPT_COMMAND);
    public final static LocalButton NO_BUTTON = new LocalButton("Неа", DECLINE_COMMAND);
    public final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);

    public final static LocalMessage TRY_AGAIN = new LocalMessageBuilder().text("Попробуйте снова.").build();
    public final static LocalMessage ASK_FOR_REGISTRATION = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();

    public final static String USER_INFO_TEMPLATE = "ФИО: %s\nГруппа: %s-%d0%d (%s)";
}
