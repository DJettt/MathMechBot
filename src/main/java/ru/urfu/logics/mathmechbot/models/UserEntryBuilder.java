package ru.urfu.logics.mathmechbot.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Билдер для рекорда UserEntry.
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
     * Конструктор, устанавливающий дефолтные значения для полей.
     *
     * @param id      идентификатор записи.
     * @param surname фамилия.
     * @param name    имя.
     * @param userId  идентификатор пользователя, который эту запись зарегистрировал.
     */
    public UserEntryBuilder(@NotNull Long id, @NotNull String surname, @NotNull String name, @NotNull Long userId) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronym = null;
        this.specialty = null;
        this.men = null;
        this.year = null;
        this.group = null;
        this.userId = userId;
    }

    /**
     * Конструктор, копирующий значений из объекта User.
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
     * Устанавливает поле name будущего объекта.
     *
     * @param name строка, которую нужно положить в name
     * @return себя же
     */
    public UserEntryBuilder name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Устанавливает поле name будущего объекта.
     *
     * @param surname строка, которую нужно положить в name
     * @return себя же
     */
    public UserEntryBuilder surname(@NotNull String surname) {
        this.surname = surname;
        return this;
    }

    /**
     * Устанавливает поле patronym будущего объекта.
     *
     * @param patronym строка, которую нужно положить в patronym
     * @return себя же
     */
    public UserEntryBuilder patronym(@Nullable String patronym) {
        this.patronym = patronym;
        return this;
    }

    /**
     * Устанавливает поле specialty будущего объекта.
     *
     * @param specialty строка, которую нужно положить в specialty
     * @return себя же
     */
    public UserEntryBuilder specialty(@Nullable String specialty) {
        this.specialty = specialty;
        return this;
    }

    /**
     * Устанавливает поле men будущего объекта.
     *
     * @param men строка, которую нужно положить в men
     * @return себя же
     */
    public UserEntryBuilder men(@Nullable String men) {
        this.men = men;
        return this;
    }

    /**
     * Устанавливает поле year будущего объекта.
     *
     * @param year строка, которую нужно положить в year
     * @return себя же
     */
    public UserEntryBuilder year(@Nullable Integer year) {
        this.year = year;
        return this;
    }

    /**
     * Устанавливает поле group будущего объекта.
     *
     * @param group строка, которую нужно положить в group
     * @return себя же
     */
    public UserEntryBuilder group(@Nullable Integer group) {
        this.group = group;
        return this;
    }

    /**
     * Создаёт объект UserEntry.
     *
     * @return созданный объект
     */
    public UserEntry build() {
        return new UserEntry(id, surname, name, patronym, specialty, men, year, group, userId);
    }
}
