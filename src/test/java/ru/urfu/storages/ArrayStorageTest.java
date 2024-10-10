package ru.urfu.storages;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса ArrayStorage
 */
final class ArrayStorageTest {
    private ArrayStorage<StringWithId, Integer> storage;
    private ArrayStorage<StringWithId, Integer> emptyStorage;

    /**
     * Создаём хранилище для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        storage = new ArrayStorage<>();
        storage.add(new StringWithId(1, "String 1"));
        storage.add(new StringWithId(34, "String 34"));
        storage.add(new StringWithId(5, "String 5"));
        storage.add(new StringWithId(27, "String 27"));
        emptyStorage = new ArrayStorage<>();
    }

    /**
     * <p>
     * Проверка того, что запрос всех элементов возвращает все элементы и что при добавлении
     * ещё одного элемента, повторный запрос возвращает и его.
     * </p>
     *
     * <p>
     * С этим тестом есть неприятный момент, связанный с тем, что функционал getAll проверяется
     * через add, а функционал - add через getAll. Изолированно проверить оба метода не получается.
     * </p>
     *
     * <ol>
     *     <li>Проверяем, что непустое хранилище возвращает все добавленные элементы.</li>
     *     <li>Проверяем, что пустое хранилище возвращает пустой список.</li>
     *     <li>Добавляем ещё один элемент в непустое хранилище.</li>
     *     <li>Проверяем, что непустое хранилище возвращает все добавленные элементы, включая добавленный.</li>
     * </ol>
     */
    @Test
    @DisplayName("Добавление элементов и запрос всех элементов")
    void testAddAndGetAll() {
        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                )),
                storage.getAll());
        Assertions.assertEquals(new ArrayList<>(), emptyStorage.getAll());

        storage.add(new StringWithId(2, "String 2"));
        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27"),
                        new StringWithId(2, "String 2")
                )),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при добавлении элемента с уже id бросается исключение.</p>
     *
     * <ol>
     *     <li>Проверяем, что добавление того же элемента с уже занятым id кидает исключение.</li>
     *     <li>Проверяем, что добавление изменённого элемента с уже занятым id кидает исключение.</li>
     *     <li>Проверяем, что содержимое хранилища не изменилось.</li>
     * </ol>
     */
    @Test
    @DisplayName("Добавление уже существующего элемента")
    void testAddExisting() {
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> storage.add(new StringWithId(1, "String 1")));
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> storage.add(new StringWithId(1, "Modified String 1")));

        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                )),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что поиск существующего элемента возвращает этот элемент.</p>
     *
     * <ol>
     *     <li>Ищем существующий.</li>
     *     <li>Проверяем, что поиск вернул его.</li>
     * </ol>
     */
    @Test
    @DisplayName("Поиск существующего элемента")
    void testGetExisting() {
        Assertions.assertTrue(storage.get(5).isPresent());
        Assertions.assertEquals(
                new StringWithId(5, "String 5"),
                storage.get(5).get());
    }

    /**
     * <p>Проверка того, что поиск несуществующего элемента не возвращает ничего.</p>
     *
     * <ol>
     *     <li>Ищем несуществующий.</li>
     *     <li>Проверяем, что ничего не нашлось ни в пустом, ни в непустом хранилище.</li>
     * </ol>
     */
    @Test
    @DisplayName("Поиск несуществующего элемента")
    void testGetNonexistent() {
        Assertions.assertTrue(storage.get(2).isEmpty());
        Assertions.assertTrue(emptyStorage.get(2).isEmpty());
    }

    /**
     * <p>Проверка того, что при обновлении существующего элемента, этот элемент обновляется.</p>
     * <ol>
     *     <li>Модифицируем существующий.</li>
     *     <li>Проверяем, что хранилище содержит все элементы, но модифицированный - изменён.</li>
     * </ol>
     */
    @Test
    @DisplayName("Обновление существующего элемента")
    void testUpdateExisting() {
        storage.update(new StringWithId(5, "Modified String 5"));
        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "Modified String 5"),
                        new StringWithId(27, "String 27")
                )),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при обновлении несуществующего элемента, содержимое хранилища не меняется.</p>
     *
     * <ol>
     *     <li>Модифицируем несуществующий.</li>
     *     <li>Проверяем, что содержимое хранилища не изменилось ни в пустом, ни в непустом хранилище.</li>
     * </ol>
     */
    @Test
    @DisplayName("Обновление несуществующего элемента")
    void testUpdateNonexistent() {
        storage.update(new StringWithId(2, "Modified String 2"));
        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                )),
                storage.getAll());
        Assertions.assertEquals(new ArrayList<>(), emptyStorage.getAll());
    }

    /**
     * <p>Проверка того, что при удалении существующего элемента, этот элемент обновляется.</p>
     *
     * <ol>
     *     <li>Удаляем существующий.</li>
     *     <li>Проверяем, что хранилище содержит все элементы, кроме удалённого.</li>
     * </ol>
     */
    @Test
    @DisplayName("Удаление существующего элемента")
    void testDeleteExisting() {
        storage.delete(new StringWithId(5, "Modified String 5"));
        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(27, "String 27")
                )),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при удалении несуществующего элемента, содержимое хранилища не меняется.</p>
     *
     * <ol>
     *     <li>Удаляем несуществующий.</li>
     *     <li>Проверяем, что содержимое хранилища не изменилось ни в пустом, ни в непустом хранилище.</li>
     * </ol>
     */
    @Test
    @DisplayName("Удаление несуществующего элемента")
    void testDeleteNonexistent() {
        storage.delete(new StringWithId(2, "Modified String 2"));
        Assertions.assertEquals(new ArrayList<>(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                )),
                storage.getAll());
        Assertions.assertEquals(new ArrayList<>(), emptyStorage.getAll());
    }
}