package ru.urfu.mathmechbot;

/**
 * <p>Ряд констант, используемый во всех состояниях MathMechBot.</p>
 */
public final class Constants {
    /**
     * <p>Приватный конструктор, так как
     * не нужно создавать объект этого класса.</p>
     */
    private Constants() {
    }

    public final static String ACCEPT_COMMAND = "/yes";
    public final static String DECLINE_COMMAND = "/no";
    public final static String BACK_COMMAND = "/back";

    public final static String EDITING_FULL_NAME = "full_name";
    public final static String EDITING_YEAR = "year";
    public final static String EDITING_SPECIALITY = "speciality";
    public final static String EDITING_GROUP = "number_of_group";
    public final static String EDITING_MEN = "men";
}
