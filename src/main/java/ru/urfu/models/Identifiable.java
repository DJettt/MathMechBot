package ru.urfu.models;

/**
 * Интерфейс, указывающий на то, что у объекта можно выделить id.
 * @param <I> тип идентификатора
 */
public interface Identifiable<I> {
    /**
     * Идентификатор.
     * @return идентификатор.
     */
    I id();
}
