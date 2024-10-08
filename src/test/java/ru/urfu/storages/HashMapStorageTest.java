package ru.urfu.storages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Тесты для класса HashMapStorage
 */
class HashMapStorageTest {
    private HashMapStorage<StringWithId, Integer> arrayStorage;

    /**
     * Создаём хранилище для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        arrayStorage = new HashMapStorage<>();
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
        arrayStorage.add(new StringWithId(6, "String"));
        arrayStorage.add(new StringWithId(2, "String 2"));
        Assertions.assertEquals("String", arrayStorage.getById(6).str());
        Assertions.assertEquals("String 2", arrayStorage.getById(2).str());
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
        arrayStorage.add(new StringWithId(1, "String"));
        final StringWithId found = arrayStorage.getById(2);
        Assertions.assertNull(found);
    }
}