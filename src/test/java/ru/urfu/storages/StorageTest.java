package ru.urfu.storages;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * <p>Тесты для различных реализаций Storage.</p>
 *
 * <p>SuppressWarnings: считаю, что в данном случае повторения делают тесты прозрачнее.</p>
 */
@SuppressWarnings({"MagicNumber", "MultipleStringLiterals"})
@DisplayName("Реализации хранилищ (Storage)")
final class StorageTest {
    /**
     * <p>Добавляет в хранилище элементы для тестов.</p>
     *
     * @param storage хранилище, в которое добавляем элементы.
     */
    private void addElements(Storage<StringWithId, Integer> storage) {
        storage.add(new StringWithId(1, "String 1"));
        storage.add(new StringWithId(34, "String 34"));
        storage.add(new StringWithId(5, "String 5"));
        storage.add(new StringWithId(27, "String 27"));
    }

    /**
     * <p>Тестируем различные реализации хранилищ сразу же.</p>
     *
     * @return аргументы для теста (объект тестируемой реализации хранилища).
     */
    static Arguments[] storages() {
        return new Arguments[]{
                Arguments.of(new ArrayStorage<StringWithId, Integer>())
        };
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
     *     <li>Проверяем, что пустое хранилище возвращает пустой список.</li>
     *     <li>Добавляем элементы.</li>
     *     <li>Проверяем, что хранилище возвращает все добавленные элементы.</li>
     *     <li>Добавляем ещё один элемент.</li>
     *     <li>Проверяем, что хранилище возвращает все добавленные элементы, включая добавленный.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Добавление элементов и запрос всех элементов")
    @MethodSource("storages")
    @ParameterizedTest
    void testAddAndGetAll(Storage<StringWithId, Integer> storage) {
        Assertions.assertEquals(List.of(), storage.getAll());

        addElements(storage);

        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                ),
                storage.getAll());

        storage.add(new StringWithId(2, "String 2"));
        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27"),
                        new StringWithId(2, "String 2")
                ),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при добавлении элемента с уже id бросается исключение.</p>
     *
     * <ol>
     *     <li>Добавляем элементы.</li>
     *     <li>Проверяем, что добавление того же элемента с уже занятым id кидает исключение.</li>
     *     <li>Проверяем, что добавление изменённого элемента с уже занятым id кидает исключение.</li>
     *     <li>Проверяем, что содержимое хранилища не изменилось.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Добавление уже существующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testAddExisting(Storage<StringWithId, Integer> storage) {
        addElements(storage);

        Assertions.assertThrows(
                IllegalArgumentException.class, () -> storage.add(new StringWithId(1, "String 1")));
        Assertions.assertThrows(
                IllegalArgumentException.class, () -> storage.add(new StringWithId(1, "Modified String 1")));

        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                ),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что поиск существующего элемента возвращает этот элемент.</p>
     *
     * <ol>
     *     <li>Добавляем элементы.</li>
     *     <li>Ищем существующий элемент.</li>
     *     <li>Проверяем, что поиск вернул его.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Поиск существующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testGetExisting(Storage<StringWithId, Integer> storage) {
        addElements(storage);

        Assertions.assertTrue(storage.get(5).isPresent());
        Assertions.assertEquals(
                new StringWithId(5, "String 5"),
                storage.get(5).get());
    }

    /**
     * <p>Проверка того, что поиск несуществующего элемента не возвращает ничего.</p>
     *
     * <ol>
     *     <li>Проверяем, что ничего не нашлось в пустом хранилище.</li>
     *     <li>Добавляем элементы.</li>
     *     <li>Ищем несуществующий.</li>
     *     <li>Проверяем, что ничего не нашлось.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Поиск несуществующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testGetNonexistent(Storage<StringWithId, Integer> storage) {
        Assertions.assertTrue(storage.get(2).isEmpty());
        addElements(storage);
        Assertions.assertTrue(storage.get(2).isEmpty());
    }

    /**
     * <p>Проверка того, что при обновлении существующего элемента, этот элемент обновляется.</p>
     *
     * <ol>
     *     <li>Добавляем элементы.</li>
     *     <li>Модифицируем существующий.</li>
     *     <li>Проверяем, что хранилище содержит все элементы, но модифицированный - изменён.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Обновление существующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testUpdateExisting(Storage<StringWithId, Integer> storage) {
        addElements(storage);
        storage.update(new StringWithId(5, "Modified String 5"));
        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "Modified String 5"),
                        new StringWithId(27, "String 27")
                ),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при обновлении несуществующего элемента, содержимое хранилища не меняется.</p>
     *
     * <ol>
     *     <li>Модифицируем несуществующий элемент в пустом хранилище</li>
     *     <li>Проверяем, что хранилище всё также пусто.</li>
     *     <li>Добавляем элементы.</li>
     *     <li>Модифицируем снова.</li>
     *     <li>Проверяем, что содержимое хранилища не изменилось.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Обновление несуществующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testUpdateNonexistent(Storage<StringWithId, Integer> storage) {
        storage.update(new StringWithId(2, "Modified String 2"));
        Assertions.assertEquals(List.of(), storage.getAll());

        addElements(storage);

        storage.update(new StringWithId(2, "Modified String 2"));
        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")
                ),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при удалении существующего элемента, этот элемент обновляется.</p>
     *
     * <ol>
     *     <li>Добавляем элементы.</li>
     *     <li>Удаляем существующий.</li>
     *     <li>Проверяем, что хранилище содержит все элементы, кроме удалённого.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Удаление существующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testDeleteExisting(Storage<StringWithId, Integer> storage) {
        addElements(storage);
        storage.delete(new StringWithId(5, "Modified String 5"));
        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(27, "String 27")
                ),
                storage.getAll());
    }

    /**
     * <p>Проверка того, что при удалении несуществующего элемента, содержимое хранилища не меняется.</p>
     *
     * <ol>
     *     <li>Удаляем несуществующий элемент в пустом хранилище.</li>
     *     <li>Проверяем, что хранилище всё так же пусто.</li>
     *     <li>Добавляем элементы.</li>
     *     <li>Удаляем несуществующий.</li>
     *     <li>Проверяем, что содержимое хранилища не изменилось.</li>
     * </ol>
     *
     * @param storage тестируемая реализация Storage.
     */
    @DisplayName("Удаление несуществующего элемента")
    @MethodSource("storages")
    @ParameterizedTest
    void testDeleteNonexistent(Storage<StringWithId, Integer> storage) {
        storage.delete(new StringWithId(2, "Modified String 2"));
        Assertions.assertEquals(List.of(), storage.getAll());

        addElements(storage);

        storage.delete(new StringWithId(2, "Modified String 2"));
        Assertions.assertEquals(List.of(
                        new StringWithId(1, "String 1"),
                        new StringWithId(34, "String 34"),
                        new StringWithId(5, "String 5"),
                        new StringWithId(27, "String 27")),
                storage.getAll());
    }
}
