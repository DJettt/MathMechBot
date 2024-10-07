package ru.urfu;

/**
 * Объект, содержащий в себе все кнопки,
 * которые должны быть в сообщении бота.
 */
public class LocalButton {
    private final String name;
    private final String data;

    /**
     * Конструктор.
     * @param name имя на кнопке
     * @param data возвращаемое значение
     */
    public LocalButton(String name, String data) {
        this.name = name;
        this.data = data;
    }

    /**
     * Геттер поля name.
     * @return содержимое поля name
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер поля data.
     * @return содержимое поля data
     */
    public String getData() {
        return data;
    }
}
