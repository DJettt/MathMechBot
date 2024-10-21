package ru.urfu.logics.mathmechbot;

import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;

/**
 * Ряд констант, используемый во всех состояниях MathMechBot.
 */
public final class Constants {
    public final static String ACCEPT_COMMAND = "/yes";
    public final static String DECLINE_COMMAND = "/no";
    public final static String BACK_COMMAND = "/back";

    public final LocalButton yesButton = new LocalButton("Да", ACCEPT_COMMAND);
    public final LocalButton noButton = new LocalButton("Нет", DECLINE_COMMAND);
    public final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);

    public final LocalMessage tryAgain = new LocalMessage("Попробуйте снова.");
    public final LocalMessage askForRegistration = new LocalMessage("Сперва нужно зарегистрироваться.");
}
