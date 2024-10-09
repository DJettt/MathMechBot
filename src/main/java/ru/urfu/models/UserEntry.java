package ru.urfu.models;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Модель введённых пользователем данных.
 */
public final class UserEntry implements Identifiable<Long> {
    private final Long id;
    @Nullable
    private String name;
    @Nullable
    private final String surname;
    @Nullable
    private final String patronym;
    @Nullable
    private String specialty;
    @Nullable
    private String men;
    @Nullable
    private String year;
    @Nullable
    private String group;
    private final Long userId;

    /**
     * @param id        идентификатор введённых данных.
     * @param name      имя человека, упоминания которого мы ищем.
     * @param surname   фамилия того же.
     * @param patronym  отчество того же (если есть).
     * @param specialty направление подготовки того же человека.
     * @param year      курс.
     * @param group     номер группы того же человека;
     * @param userId    идентификатор модели User того, указывающий на того, кому пересылать упоминания этого человека.
     */
    public UserEntry(
            Long id,
            @Nullable String name,
            @Nullable String surname,
            @Nullable String patronym,
            @Nullable String specialty,
            @Nullable String men,
            @Nullable String year,
            @Nullable String group,
            Long userId
    ) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
        this.specialty = specialty;
        this.men = men;
        this.year = year;
        this.group = group;
        this.userId = userId;
    }

    @Override
    public Long id() {
        return id;
    }

    @Nullable
    public String name() {
        return name;
    }
    public void setName(@Nullable String fullName) {
        this.name = fullName;
    }

    @Nullable
    public String surname() {
        return surname;
    }

    @Nullable
    public String patronym() {
        return patronym;
    }

    @Nullable
    public String specialty() {
        return specialty;
    }

    public void setSpecialty(@Nullable String spec) {
        this.specialty = spec;
    }

    @Nullable
    public String men() {
        return men;
    }

    public void setMen(@Nullable String text) {
        this.men = text;
    }

    public String year() {
        return year;
    }
    public void setYear(@Nullable String year) {
        this.year = year;
    }

    public String group() {
        return group;
    }
    public void setGroup(@Nullable String text) {
        this.group = text;
    }

    public Long userId() {
        return userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserEntry) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.surname, that.surname) &&
                Objects.equals(this.patronym, that.patronym) &&
                Objects.equals(this.specialty, that.specialty) &&
                Objects.equals(this.men, that.men) &&
                Objects.equals(this.year, that.year) &&
                Objects.equals(this.group, that.group) &&
                Objects.equals(this.userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronym, specialty, men, year, group, userId);
    }

    @Override
    public String toString() {
        return "UserEntry[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "surname=" + surname + ", " +
                "patronym=" + patronym + ", " +
                "specialty=" + specialty + ", " +
                "men=" + men + ", " +
                "year=" + year + ", " +
                "group=" + group + ", " +
                "userId=" + userId + ']';
    }
}
