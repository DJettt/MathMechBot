package ru.urfu.storages;

import java.util.List;
import java.util.Optional;

/**
 * <p>Интерфейс хранилища, некоторой абстракции над данными.</p>
 *
 * @param <T> тип хранимых значений
 * @param <I> тип идентификатора у хранимого значения
 */
public interface Storage<T extends Identifiable<I>, I> {
    /**
     * <p>Ищет элемент с данным идентификатором.</p>
     *
     * @param id идентификатор.
     * @return первый элемент из хранилища с данным идентификатором.
     */
    Optional<T> get(I id);

    /**
     * <p>Возвращает все элементы.</p>
     *
     * @return все элементы хранилища.
     */
    List<T> getAll();

    /**
     * <p>Добавляет элемент в хранилище.</p>
     *
     * <p>Должен бросать IllegalArgumentException, если элемент
     * с данным id уже существует внутри хранилища.</p>
     *
     * @param member элемент, который добавляем.
     */
    void add(T member) throws IllegalArgumentException;

    /**
     * <p>Обновляет элемент в хранилище.</p>
     *
     * <p>Два элемента считаются одним и тем же, если их id совпадает.</p>
     *
     * @param member элемент, который хотим обновить.
     */
    void update(T member);

    /**
     * <p>Удаляет элемент из хранилища.</p>
     *
     * <p>Два элемента считаются одним и тем же, если их id совпадает.</p>
     *
     * @param member элемент, который хотим удалить.
     */
    void delete(T member);
}
