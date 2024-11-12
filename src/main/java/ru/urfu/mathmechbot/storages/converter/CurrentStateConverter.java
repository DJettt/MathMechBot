package ru.urfu.mathmechbot.storages.converter;


import ru.urfu.mathmechbot.UserState;

/**
 * Конвертирует строку из бд в конкретное состояние и наоборот.
 */
public class CurrentStateConverter {
    private final static String DEFAULT = "DEFAULT";
    private final static String REGISTRATION_NAME = "REGISTRATION_NAME";
    private final static String REGISTRATION_YEAR = "REGISTRATION_YEAR";
    private final static String REGISTRATION_SPECIALTY = "REGISTRATION_SPECIALTY";
    private final static String REGISTRATION_GROUP = "REGISTRATION_GROUP";
    private final static String REGISTRATION_MEN = "REGISTRATION_MEN";
    private final static String REGISTRATION_CONFIRMATION = "REGISTRATION_CONFIRMATION";
    private final static String EDITING_CHOOSE = "EDITING_CHOOSE";
    private final static String EDITING_FULL_NAME = "EDITING_FULL_NAME";
    private final static String EDITING_YEAR = "EDITING_YEAR";
    private final static String EDITING_SPECIALITY = "EDITING_SPECIALITY";
    private final static String EDITING_GROUP = "EDITING_GROUP";
    private final static String EDITING_MEN = "EDITING_MEN";
    private final static String EDITING_ADDITIONAL_EDIT = "EDITING_ADDITIONAL_EDIT";
    private final static String DELETION_CONFIRMATION = "DELETION_CONFIRMATION";


    public CurrentStateConverter() {
    }

    /**
     * Переводит UserState -> StateName.
     * @param state состояние пользователя
     * @return название состояния
     */
    public String convert(UserState state) {
        return switch (state) {
            case UserState.REGISTRATION_NAME -> REGISTRATION_NAME;
            case UserState.REGISTRATION_YEAR -> REGISTRATION_YEAR;
            case UserState.REGISTRATION_SPECIALTY -> REGISTRATION_SPECIALTY;
            case UserState.REGISTRATION_GROUP -> REGISTRATION_GROUP;
            case UserState.REGISTRATION_MEN -> REGISTRATION_MEN;
            case UserState.REGISTRATION_CONFIRMATION -> REGISTRATION_CONFIRMATION;
            case UserState.EDITING_CHOOSE -> EDITING_CHOOSE;
            case UserState.EDITING_FULL_NAME -> EDITING_FULL_NAME;
            case UserState.EDITING_YEAR -> EDITING_YEAR;
            case UserState.EDITING_SPECIALITY -> EDITING_SPECIALITY;
            case UserState.EDITING_GROUP -> EDITING_GROUP;
            case UserState.EDITING_MEN -> EDITING_MEN;
            case UserState.EDITING_ADDITIONAL_EDIT -> EDITING_ADDITIONAL_EDIT;
            case UserState.DELETION_CONFIRMATION -> DELETION_CONFIRMATION;
            default -> DEFAULT;
        };
    }

    /**
     * Переводит StateName -> UserState.
     * @param stateName состояние пользователя
     * @return состояние
     */
    public UserState convert(String stateName) {
        return switch(stateName) {
            case REGISTRATION_NAME -> UserState.REGISTRATION_NAME;
            case REGISTRATION_YEAR -> UserState.REGISTRATION_YEAR;
            case REGISTRATION_SPECIALTY -> UserState.REGISTRATION_SPECIALTY;
            case REGISTRATION_GROUP -> UserState.REGISTRATION_GROUP;
            case REGISTRATION_MEN -> UserState.REGISTRATION_MEN;
            case REGISTRATION_CONFIRMATION -> UserState.REGISTRATION_CONFIRMATION;
            case EDITING_CHOOSE -> UserState.EDITING_CHOOSE;
            case EDITING_FULL_NAME -> UserState.EDITING_FULL_NAME;
            case EDITING_YEAR -> UserState.EDITING_YEAR;
            case EDITING_SPECIALITY -> UserState.EDITING_SPECIALITY;
            case EDITING_GROUP -> UserState.EDITING_GROUP;
            case EDITING_MEN -> UserState.EDITING_MEN;
            case EDITING_ADDITIONAL_EDIT -> UserState.EDITING_ADDITIONAL_EDIT;
            case DELETION_CONFIRMATION -> UserState.DELETION_CONFIRMATION;
            default -> UserState.DEFAULT;
        };
    }
}
