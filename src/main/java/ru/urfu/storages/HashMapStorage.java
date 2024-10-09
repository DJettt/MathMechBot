package ru.urfu.storages;

import java.util.HashMap;
import ru.urfu.logics.mathmechbot.models.Identifiable;

/**
 * Хранилище данных, реализованное на хэш-мапе.
 * @param <T> тип хранимого значения.
 * @param <I> тип идентификатора у хранимых объектов.
 */
public class HashMapStorage<T extends Identifiable<I>, I> implements Storage<T, I> {
    final HashMap<I, T> hashMap;

    /**
     * Конструктор.
     */
    public HashMapStorage() {
        hashMap = new HashMap<>();
    }

    @Override
    public void add(T member) {
        hashMap.put(member.id(), member);
    }

    @Override
    public T getById(I id) {
        return hashMap.get(id);
    }

    @Override
    public void deleteById(I id) {
        hashMap.remove(id);
    }
}
