package ru.urfu.storages;

import java.util.ArrayList;
import ru.urfu.models.Identifiable;

/**
 * Хранилище данных, реализованное на простом массиве.
 * @param <T> тип хранимого значения.
 * @param <I> тип идентификатора у хранимых объектов.
 */
public class ArrayStorage<T extends Identifiable<I>, I> implements Storage<T, I> {
    private final ArrayList<T> array;

    /**
     * Конструктор.
     */
    public ArrayStorage() {
        array = new ArrayList<>();
    }

    @Override
    public void add(T member) {
        array.add(member);
    }

    @Override
    public T getById(I id) {
        for (T t : array) {
            if (t.id() == id) {
                return t;
            }
        }
        return null;
    }
}
