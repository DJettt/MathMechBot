package ru.urfu.logics.localobjects;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Абстракция над кнопками на различных платформах.</p>
 *
 * @param name текст в кнопке
 * @param data информация, которая возвращается при нажатии на кнопку
 */
public record LocalButton(@NotNull String name, @NotNull String data) {
}
