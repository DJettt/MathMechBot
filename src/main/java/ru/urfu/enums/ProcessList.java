package ru.urfu.enums;

/**
 * Перечисление всех процессов.
 * Процесс - это совокупность набора состояний.
 */
public interface ProcessList {
    /**
     * Возвращает класс списка состояний.
     *
     * @return класс списка состояний.
     */
    Class<? extends StateList> stateClass();
}
