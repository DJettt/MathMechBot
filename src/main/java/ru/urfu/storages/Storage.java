package ru.urfu.storages;

import ru.urfu.models.Identifiable;

/**
 * Интерфейс хранилища, некоторой абстракции над данными.
 * @param <T> тип хранимых значений
 * @param <I> тип идентификатора у хранимого значения
 */
public interface Storage<T extends Identifiable<I>, I> {
    /**
     * Добавляет элемент в хранилище.
     * @param member элемент, который добавляем.
     */
    void add(T member);

    /**
     * Ищет элемент с данным идентификатором.
     * @param id идентификатор.
     * @return первый элемент из хранилища с данным идентификатором.
     */
    T getById(I id);
}
