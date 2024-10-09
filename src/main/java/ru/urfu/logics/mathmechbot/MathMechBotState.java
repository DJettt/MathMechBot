package ru.urfu.logics.mathmechbot;

import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;

public abstract class MathMechBotState extends State {
    protected final static String ACCEPT_COMMAND = "/yes";
    protected final static String DECLINE_COMMAND = "/no";
    protected final static String BACK_COMMAND = "/back";

    protected final static LocalButton YES_BUTTON = new LocalButton("Да", ACCEPT_COMMAND);
    protected final static LocalButton NO_BUTTON = new LocalButton("Неа", DECLINE_COMMAND);
    protected final static LocalButton BACK_BUTTON = new LocalButton("Назад", BACK_COMMAND);

    protected final static LocalMessage TRY_AGAIN = new LocalMessageBuilder()
            .text("Попробуйте снова.")
            .build();

    protected final static String USER_INFO_TEMPLATE = """
            ФИО: %s
            Группа: %s-%s0%s (%s)""";

    protected final MathMechBotCore context;

    public MathMechBotState(MathMechBotCore context) {
        this.context = context;
    }
}