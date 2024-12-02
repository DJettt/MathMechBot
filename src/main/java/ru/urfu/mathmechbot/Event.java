package ru.urfu.mathmechbot;

/**
 * <p>События, возможные в FSM MathMechBot'а.</p>
 */
public enum Event {
    ACCEPT,
    DECLINE,
    BACK,

    HELP,
    ALREADY_REGISTERED,
    NOT_REGISTERED,

    REGISTER,
    INFO,
    EDIT,
    DELETE,

    INVALID_INPUT,
    VALID_INPUT,

    FULL_NAME_CHOSEN,
    YEAR_CHOSEN,
    SPECIALTY_CHOSEN,
    GROUP_CHOSEN,
    MEN_CHOSEN
}
