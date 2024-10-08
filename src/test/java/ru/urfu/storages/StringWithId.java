package ru.urfu.storages;

import org.jetbrains.annotations.NotNull;
import ru.urfu.models.Identifiable;

/**
 * Строка с идентификатором. Простой класс для тестирования хранилищ.
 * @param id идентификатор.
 * @param str строка.
 */
public record StringWithId(Integer id, @NotNull String str) implements Identifiable<Integer> {
}
