package ru.urfu.storages;

import java.util.List;
import java.util.Optional;
import ru.urfu.logics.mathmechbot.models.Identifiable;

/**
 * Интерфейс хранилища, некоторой абстракции над данными.
 * @param <T> тип хранимых значений
 * @param <I> тип идентификатора у хранимого значения
 */
public interface Storage<T extends Identifiable<I>, I> {
    /**
     * Ищет элемент с данным идентификатором.
     * @param id идентификатор.
     * @return первый элемент из хранилища с данным идентификатором.
     */
    Optional<T> get(I id);

    /**
     * Возвращает все элементы.
     *
     * @return все элементы хранилища.
     */
    List<T> getAll();

    /**
     * Добавляет элемент в хранилище.
     *
     * @param member элемент, который добавляем.
     */
    void add(T member) throws IllegalArgumentException;

    /**
     * Обновляет элемент в хранилище.<br/>
     * Два элемента считаются одним и тем же, если их id совпадает.
     *
     * @param member элемент, который хотим обновить.
     */
    void update(T member);

    /**
     * Удаляет элемент из хранилища.<br/>
     * Два элемента считаются одним и тем же, если их id совпадает.
     *
     * @param member элемент, который хотим удалить.
     */
    void delete(T member);
}
