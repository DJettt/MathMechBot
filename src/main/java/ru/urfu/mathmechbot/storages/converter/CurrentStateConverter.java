package ru.urfu.mathmechbot.storages.converter;


import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private final BiMap<String, UserState> biMap = HashBiMap.create();

    /**
     * Конструктор.
     */
    public CurrentStateConverter() {
        biMap.put(REGISTRATION_NAME, UserState.REGISTRATION_NAME);
        biMap.put(REGISTRATION_YEAR, UserState.REGISTRATION_YEAR);
        biMap.put(REGISTRATION_SPECIALTY, UserState.REGISTRATION_SPECIALTY);
        biMap.put(REGISTRATION_GROUP, UserState.REGISTRATION_GROUP);
        biMap.put(REGISTRATION_MEN, UserState.REGISTRATION_MEN);
        biMap.put(REGISTRATION_CONFIRMATION, UserState.REGISTRATION_CONFIRMATION);
        biMap.put(EDITING_CHOOSE, UserState.EDITING_CHOOSE);
        biMap.put(EDITING_FULL_NAME, UserState.EDITING_FULL_NAME);
        biMap.put(EDITING_YEAR, UserState.EDITING_YEAR);
        biMap.put(EDITING_SPECIALITY, UserState.EDITING_SPECIALITY);
        biMap.put(EDITING_GROUP, UserState.EDITING_GROUP);
        biMap.put(EDITING_MEN, UserState.EDITING_MEN);
        biMap.put(EDITING_ADDITIONAL_EDIT, UserState.EDITING_ADDITIONAL_EDIT);
        biMap.put(DELETION_CONFIRMATION, UserState.DELETION_CONFIRMATION);
        biMap.put(DEFAULT, UserState.DEFAULT);
    }

    /**
     * Переводит UserState -> StateName.
     * @param state состояние пользователя
     * @return название состояния
     */
    public String convert(UserState state) {
        return biMap.inverse().get(state);
    }

    /**
     * Переводит StateName -> UserState.
     * @param stateName состояние пользователя
     * @return состояние
     */
    @Nullable
    public UserState convert(@NotNull String stateName) {
        return biMap.get(stateName);
    }
}





