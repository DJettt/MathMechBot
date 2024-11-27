package ru.urfu.cache;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.userentry.UserEntryCacheStorage;

/**
 * Тесты внутреннего кэша UserEntryCacheStorage.
 */
public class UserEntryCacheTest {
    private final static int MAX_SIZE = 2;
    private final static String TEST_SURNAME = "Иванов";
    private final static String TEST_NAME = "Иван";
    private final static String TEST_PATRONYM = "Иванович";
    private final static String TEST_SPECIALITY = "МХ";
    private final static int TEST_GROUP = 2;
    private final static int TEST_YEAR = 2;
    private final static String TEST_MEN = "МЕН-123456";
    private final static long TEST_ID = 0L;
    private final static String NEW_TEST_SURNAME = "Сергеев";
    private final static String NEW_TEST_NAME = "Сергей";
    private final static String NEW_TEST_PATRONYM = "Сергеевич";
    private final static String NEW_TEST_SPECIALITY = "МТ";
    private final static int NEW_TEST_GROUP = 3;
    private final static int NEW_TEST_YEAR = 3;
    private final static String NEW_TEST_MEN = "МЕН-333333";
    private final UserEntry testMember = new UserEntryBuilder(TEST_ID, TEST_ID)
            .surname(TEST_SURNAME)
            .name(TEST_NAME)
            .patronym(TEST_PATRONYM)
            .specialty(TEST_SPECIALITY)
            .group(TEST_GROUP)
            .year(TEST_YEAR)
            .men(TEST_MEN)
            .build();
    private final UserEntry newMember = new UserEntryBuilder(TEST_ID, TEST_ID)
            .surname(NEW_TEST_SURNAME)
            .name(NEW_TEST_NAME)
            .patronym(NEW_TEST_PATRONYM)
            .specialty(NEW_TEST_SPECIALITY)
            .group(NEW_TEST_GROUP)
            .year(NEW_TEST_YEAR)
            .men(NEW_TEST_MEN)
            .build();
    private final UserEntry testMember2 = new UserEntryBuilder(1L, 1L)
            .surname("Константинов")
            .name("Константин")
            .patronym("Константинович")
            .specialty("КН")
            .group(1)
            .year(2)
            .men("МЕН-122222")
            .build();
    UserEntryCacheStorage cache = new UserEntryCacheStorage(MAX_SIZE);

    /**
     * Первоначальные настройки тестов.
     * В кэше находится только 1 объект
     */
    @BeforeEach
    void setupTest() {
        cache.add(testMember);
    }

    /**
     * Проверяет кэш на очищение при переполнении.
     */
    @Test
    @DisplayName("Overflow")
    void testOverflow() {
        cache.add(testMember2);
        UserEntry member = new UserEntryBuilder(2L, 2L)
                .surname("Петров")
                .name("Пётр")
                .patronym("Петрович")
                .specialty("ПМ")
                .group(1)
                .year(2)
                .men("МЕН-111111")
                .build();
        cache.add(member);
        Assertions.assertTrue(cache.get(TEST_ID).isEmpty());
    }

    /**
     * Тестирует метод getAll у кэша UserEntryCacheStorage.
     */
    @Test
    @DisplayName("Тест getAll")
    void testGetAll() {
        cache.add(testMember2);
        Assertions.assertEquals(List.of(testMember, testMember2), cache.getAll());
    }

    /**
     * Тестирует разовое полное обновление информации у пользователя.
     */
    @Test
    @DisplayName("Тест полного обновления информации.")
    void testUpdate() {
        cache.update(newMember);
        Assertions.assertEquals(Optional.of(newMember), cache.get(TEST_ID));
    }

    /**
     * Тестирует все методы кэша которые изменяют какое-то конкретное поле.
     * Отличие от предыдущего теста в том, что он использует не разовое обновление
     * всего объекта, а изменяет каждое его поле по очереди.
     */
    @Test
    @DisplayName("Методы change...")
    void changeAllInfo() {
        cache.changeUserEntrySurname(TEST_ID, NEW_TEST_SURNAME);
        cache.changeUserEntryName(TEST_ID, NEW_TEST_NAME);
        cache.changeUserEntryPatronym(TEST_ID, NEW_TEST_PATRONYM);
        cache.changeUserEntrySpecialty(TEST_ID, NEW_TEST_SPECIALITY);
        cache.changeUserEntryYear(TEST_ID, NEW_TEST_YEAR);
        cache.changeUserEntryGroup(TEST_ID, NEW_TEST_GROUP);
        cache.changeUserEntryMen(TEST_ID, NEW_TEST_MEN);
        Assertions.assertEquals(newMember, cache.get(TEST_ID).orElseThrow());
    }

    /**
     * Тестирует удаление объекта из cache.
     */
    @Test
    @DisplayName("Проверка на удаление.")
    void testDelete() {
        cache.delete(testMember);
        Assertions.assertTrue(cache.get(TEST_ID).isEmpty());
    }
}
