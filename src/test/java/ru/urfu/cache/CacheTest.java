package ru.urfu.cache;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.UserEntryCacheStorage;

/**
 * Тесты внутреннего кэша.
 */
public class CacheTest {
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
    private final UserEntry member = new UserEntryBuilder(TEST_ID, TEST_ID)
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
    UserEntryCacheStorage cache = new UserEntryCacheStorage(MAX_SIZE);

    /**
     * Первоначальные настройки тестов.
     * В эше находится только 1 объект
     */
    @BeforeEach
    void setupTest() {
        cache.add(member);
    }

    /**
     * Проверяет кэш на очищение при переполнении.
     */
    @Test
    @DisplayName("Overflow")
    void testOverflow() {
        UserEntry testMember = new UserEntryBuilder(1L, 1L)
                .surname("Константинов")
                .name("Константин")
                .patronym("Константинович")
                .specialty("КН")
                .group(1)
                .year(2)
                .men("МЕН-122222")
                .build();
        cache.add(testMember);
        testMember = new UserEntryBuilder(2L, 2L)
                .surname("Петров")
                .name("Пётр")
                .patronym("Петрович")
                .specialty("ПМ")
                .group(1)
                .year(2)
                .men("МЕН-111111")
                .build();
        cache.add(testMember);
        Assertions.assertEquals(Optional.empty(), cache.get(0L));
    }

    /**
     * Тестирует разовое полное обновление информации.
     */
    @Test
    @DisplayName("Тест полного обновления.")
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
        Assertions.assertEquals(Optional.of(newMember), cache.get(TEST_ID));
    }

    /**
     * Тестирует удаление объекта из cache.
     */
    @Test
    @DisplayName("Проверка на удаление.")
    void testDelete() {
        cache.delete(member);
        Assertions.assertEquals(Optional.empty(), cache.get(TEST_ID));
    }
}
