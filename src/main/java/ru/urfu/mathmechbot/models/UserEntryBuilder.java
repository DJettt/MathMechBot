package ru.urfu.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Билдер для рекорда UserEntry.</p>
 */
public final class UserEntryBuilder {
    private final Long id;
    private String surname;
    private String name;
    private String patronym;
    private String specialty;
    private String men;
    private Integer year;
    private Integer group;
    private final Long userId;

    /**
     * <p>Конструктор, устанавливающий дефолтные значения для полей.</p>
     *
     * @param id      идентификатор записи.
     * @param userId  идентификатор пользователя, который эту запись зарегистрировал.
     */
    public UserEntryBuilder(@NotNull Long id, @NotNull Long userId) {
        this.id = id;
        this.surname = null;
        this.name = null;
        this.patronym = null;
        this.specialty = null;
        this.men = null;
        this.year = null;
        this.group = null;
        this.userId = userId;
    }

    /**
     * <p>Конструктор, копирующий значений из объекта User.</p>
     *
     * @param userEntry объект, у которого скопировать все поля.
     */
    public UserEntryBuilder(@NotNull UserEntry userEntry) {
        this.id = userEntry.id();
        this.surname = userEntry.surname();
        this.name = userEntry.name();
        this.patronym = userEntry.patronym();
        this.specialty = userEntry.specialty();
        this.men = userEntry.men();
        this.year = userEntry.year();
        this.group = userEntry.group();
        this.userId = userEntry.userId();
    }

    /**
     * <p>Устанавливает поле name будущего объекта.</p>
     *
     * @param name строка, которую нужно положить в name
     * @return себя же
     */
    public UserEntryBuilder name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * <p>Устанавливает поле name будущего объекта.</p>
     *
     * @param surname строка, которую нужно положить в name
     * @return себя же
     */
    public UserEntryBuilder surname(@NotNull String surname) {
        this.surname = surname;
        return this;
    }

    /**
     * <p>Устанавливает поле patronym будущего объекта.</p>
     *
     * @param patronym строка, которую нужно положить в patronym
     * @return себя же
     */
    public UserEntryBuilder patronym(@Nullable String patronym) {
        this.patronym = patronym;
        return this;
    }

    /**
     * <p>Устанавливает поле specialty будущего объекта.</p>
     *
     * @param specialty строка, которую нужно положить в specialty
     * @return себя же
     */
    public UserEntryBuilder specialty(@Nullable String specialty) {
        this.specialty = specialty;
        return this;
    }

    /**
     * <p>Устанавливает поле men будущего объекта.</p>
     *
     * @param men строка, которую нужно положить в men
     * @return себя же
     */
    public UserEntryBuilder men(@Nullable String men) {
        this.men = men;
        return this;
    }

    /**
     * <p>Устанавливает поле year будущего объекта.</p>
     *
     * @param year строка, которую нужно положить в year
     * @return себя же
     */
    public UserEntryBuilder year(@Nullable Integer year) {
        this.year = year;
        return this;
    }

    /**
     * <p>Устанавливает поле group будущего объекта.</p>
     *
     * @param group строка, которую нужно положить в group
     * @return себя же
     */
    public UserEntryBuilder group(@Nullable Integer group) {
        this.group = group;
        return this;
    }

    /**
     * <p>Создаёт объект UserEntry.</p>
     *
     * @return созданный объект
     */
    public UserEntry build() {
        return new UserEntry(id, surname, name, patronym, specialty, men, year, group, userId);
    }
}
