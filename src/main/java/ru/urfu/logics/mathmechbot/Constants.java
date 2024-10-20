package ru.urfu.logics.mathmechbot;

import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;

/**
 * Ряд констант, используемый во всех состояниях MathMechBot.
 */
public final class Constants {
    public final static String ACCEPT_COMMAND = "/yes";
    public final static String DECLINE_COMMAND = "/no";
    public final static String BACK_COMMAND = "/back";

    public final LocalButton yesButton = new LocalButton("Да", ACCEPT_COMMAND);
    public final LocalButton noButton = new LocalButton("Неа", DECLINE_COMMAND);
    public final LocalButton backButton = new LocalButton("Назад", BACK_COMMAND);

    public final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();
    public final LocalMessage askForRegistration = new LocalMessageBuilder()
            .text("Сперва нужно зарегистрироваться.").build();
}
