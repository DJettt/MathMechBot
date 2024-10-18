package ru.urfu.storages;

/**
 * <p>Интерфейс, указывающий на то, что у объекта можно выделить id.</p>
 *
 * @param <I> тип идентификатора
 */
public interface Identifiable<I> {
    /**
     * <p>Идентификатор.</p>
     *
     * @return идентификатор.
     */
    I id();
}
