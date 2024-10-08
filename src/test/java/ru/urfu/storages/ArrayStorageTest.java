package ru.urfu.storages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса ArrayStorage
 */
class ArrayStorageTest {
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
        Assertions.assertEquals("String", storage.getById(6).str());
        Assertions.assertEquals("String 2", storage.getById(2).str());
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
        final StringWithId found = storage.getById(2);
        Assertions.assertNull(found);
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
    @DisplayName("Удаление элемента")
    void testDeleteElement() {
        storage.add(new StringWithId(1, "String"));
        Assertions.assertEquals("String", storage.getById(1).str());

        storage.deleteById(1);
        Assertions.assertNull(storage.getById(1));
    }

    /**
     * Тестируем добавление элемента в хранилище и удаление элемента
     * <ol>
     *     <li>Добавляем в хранилище элемент с одним id.</li>
     *     <li>Проверяем, что он добавился.</li>
     *     <li>Пробуем удалить.</li>
     *     <li>Проверяем, что он удалился.</li>
     * </ol>
     */
    @Test
    @DisplayName("Удаление элемента")
    void testDeleteElementThatDontExist() {
        storage.add(new StringWithId(1, "String"));
        Assertions.assertEquals("String", storage.getById(1).str());

        storage.deleteById(2);
        Assertions.assertEquals("String", storage.getById(1).str());
    }
}