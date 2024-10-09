package ru.urfu.models;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import ru.urfu.enums.Specialty;

/**
 * Модель введённых пользователем данных.
 */
@SuppressWarnings("NonEmptyAtclauseDescription")
public final class UserEntry implements Identifiable<Long> {
    private final Long id;
    @Nullable
    private String name;
    @Nullable
    private final String surname;
    @Nullable
    private final String patronym;
    @Nullable
    private Specialty specialty;
    @Nullable
    private String men;
    @Nullable
    private String year;
    @Nullable
    private String group;
    private final Long userId;

    /**
     * Модель введенных пользователем данных.
     * @param id        идентификатор введённых данных.
     * @param name      имя человека, упоминания которого мы ищем.
     * @param surname   фамилия того же.
     * @param patronym  отчество того же (если есть).
     * @param specialty направление подготовки того же человека.
     * @param men       академическая группа
     * @param year      курс.
     * @param group     номер группы того же человека;
     * @param userId    идентификатор модели User того, указывающий на того, кому пересылать упоминания этого человека.
     */
    @SuppressWarnings("ParameterNumber")
    public UserEntry(
            Long id,
            @Nullable String name,
            @Nullable String surname,
            @Nullable String patronym,
            @Nullable Specialty specialty,
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

    /**
     * Геттер поля name.
     * @return name
     */
    @Nullable
    public String name() {
        return name;
    }

    /**
     * Сеттер поля name.
     * @param fullName
     */
    public void setName(@Nullable String fullName) {
        this.name = fullName;
    }

    /**
     * Геттер поля surname.
     * @return surname
     */
    @Nullable
    public String surname() {
        return surname;
    }

    /**
     * Геттер поля patronym.
     * @return patronym
     */
    @Nullable
    public String patronym() {
        return patronym;
    }

    /**
     * Геттер поля specialty.
     * @return specialty
     */
    @Nullable
    public Specialty specialty() {
        return specialty;
    }

    /**
     * Сеттер поля specialty.
     * @param spec - specialty
     */
    public void setSpecialty(@Nullable Specialty spec) {
        this.specialty = spec;
    }

    /**
     * Геттер поля men.
     * @return men
     */
    @Nullable
    public String men() {
        return men;
    }

    /**
     * Сеттер поля men.
     * @param text
     */
    public void setMen(@Nullable String text) {
        this.men = text;
    }

    /**
     * Геттер поля year.
     * @return year
     */
    public String year() {
        return year;
    }

    /**
     * Сеттер поля year.
     * @param year
     */
    public void setYear(@Nullable String year) {
        this.year = year;
    }

    /**
     * Геттер поля group.
     * @return group
     */
    public String group() {
        return group;
    }

    /**
     * Сеттер поля group.
     * @param text
     */
    public void setGroup(@Nullable String text) {
        this.group = text;
    }

    /**
     * Геттер поля userId.
     * @return userId
     */
    public Long userId() {
        return userId;
    }

    @SuppressWarnings("NeedBraces")
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UserEntry) obj;
        return Objects.equals(this.id, that.id)
                && Objects.equals(this.name, that.name)
                && Objects.equals(this.surname, that.surname)
                && Objects.equals(this.patronym, that.patronym)
                && Objects.equals(this.specialty, that.specialty)
                && Objects.equals(this.men, that.men)
                && this.year == that.year
                && this.group == that.group
                && Objects.equals(this.userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, patronym, specialty, men, year, group, userId);
    }

    @Override
    public String toString() {
        return "UserEntry["
                + "id=" + id + ", "
                + "name=" + name + ", "
                + "surname=" + surname + ", "
                + "patronym=" + patronym + ", "
                + "specialty=" + specialty + ", "
                + "men=" + men + ", "
                + "year=" + year + ", "
                + "group=" + group + ", "
                + "userId=" + userId + ']';
    }

}
