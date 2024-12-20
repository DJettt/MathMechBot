package ru.urfu.storages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>Хранилище данных, реализованное на простом массиве.</p>
 *
 * @param <T> тип хранимого значения.
 * @param <I> тип идентификатора у хранимых объектов.
 */
public class ArrayStorage<T extends Identifiable<I>, I> implements Storage<T, I> {
    private final List<T> array;

    /**
     * <p>Конструктор.</p>
     */
    public ArrayStorage() {
        array = new ArrayList<>();
    }

    @Override
    public Optional<T> get(I id) {
        for (final T t : array) {
            if (t.getId().equals(id)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<T> getAll() {
        final List<T> newList = new ArrayList<>(array.size());
        newList.addAll(array);
        return newList;
    }

    @Override
    public void add(T member) throws IllegalArgumentException {
        if (get(member.getId()).isPresent()) {
            throw new IllegalArgumentException("Storage already has this element.");
        }
        array.add(member);
    }

    @Override
    public void update(T member) {
        int index = 0;
        for (final T t : array) {
            if (t.getId().equals(member.getId())) {
                array.set(index, member);
            }
            ++index;
        }
    }

    @Override
    public void delete(T member) {
        array.removeIf(storedMember -> Objects.equals(storedMember.getId(), member.getId()));
    }
}
