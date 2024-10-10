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

    /**
     * Создаём хранилище для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        storage = new ArrayStorage<>();
    }

    /**
     * Тестируем добавление и изъятия элемента.
     * <ol>
     *     <li>Добавляем в хранилища два элемента.</li>
     *     <li>Изымаем элементы.</li>
     *     <li>Проверяем, что элементы содержат нужные строки.</li>
     * </ol>
     */
    @Test
    @DisplayName("Добавление и изъятие элемента")
    void testAddAndGetElement() {
        storage.add(new StringWithId(6, "String"));
        storage.add(new StringWithId(2, "String 2"));
        Assertions.assertEquals("String", storage.get(6).get().str());
        Assertions.assertEquals("String 2", storage.get(2).get().str());
    }

    /**
     * Тестируем попытку найти элемент по id, которого в хранилище нет.
     * <ol>
     *     <li>Добавляем в хранилище элемент с одним id.</li>
     *     <li>Пытаемся найти элемент с другим id</li>
     *     <li>Проверяем, такой элемент не нашёлся (вернулся null).</li>
     * </ol>
     */
    @Test
    @DisplayName("Попытка найти несуществующий элемент")
    void testNoElement() {
        storage.add(new StringWithId(1, "String"));
        Assertions.assertTrue(storage.get(2).isEmpty());
    }

    /**
     * Тестируем добавление элемента в хранилище и удаление элемента
     * <ol>
     *     <li>Добавляем в хранилище элемент с одним id.</li>
     *     <li>Проверяем, что он добавился.</li>
     *     <li>Пробуем его удалить.</li>
     *     <li>Проверяем, что он удалился.</li>
     * </ol>
     */
    @Test
    @DisplayName("Удаление существующего элемента")
    void testDeleteElement() {
        storage.add(new StringWithId(1, "String"));
        Assertions.assertEquals("String", storage.get(1).get().str());

        storage.delete(new StringWithId(1, "Modified String"));
        Assertions.assertTrue(storage.get(1).isEmpty());
    }

    /**
     * Тестируем удаление несуществующего элемента.
     * <ol>
     *     <li>Добавляем элемент.</>
     *     <li>Пробуем удалить того, чего нет.</li>
     *     <li>Проверяем, что первый элемент не удалился.</li>
     *     <li>Проверяем, что удаляемый элемент не появился.</li>
     * </ol>
     */
    @Test
    @DisplayName("Удаление несуществующего элемента")
    void testDeleteNonexistent() {
        storage.add(new StringWithId(1, "String"));
        Assertions.assertEquals("String", storage.get(1).get().str());

        storage.delete(new StringWithId(2, "String"));
        Assertions.assertEquals(new ArrayList<>(
                List.of(new StringWithId(1, "String"))
        ), storage.getAll());
    }

    @Test
    @DisplayName("Обновление существующего элемента")
    void testUpdateExisting() {

    }
}