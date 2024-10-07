package ru.urfu;

import org.jetbrains.annotations.NotNull;

/**
 * Объект, содержащий в себе все кнопки,
 * которые должны быть в сообщении бота.
 * @param name текст в кнопке
 * @param data информация, которая возвращается при нажатии на кнопку
 */
public record LocalButton(@NotNull String name, @NotNull String data) {
}
