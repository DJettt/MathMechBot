package ru.urfu.storages;

import org.jetbrains.annotations.NotNull;

/**
 * Строка с идентификатором. Простой класс для тестирования хранилищ.
 * @param getId идентификатор.
 * @param str строка.
 */
public record StringWithId(Integer getId, @NotNull String str) implements Identifiable<Integer> {
}
