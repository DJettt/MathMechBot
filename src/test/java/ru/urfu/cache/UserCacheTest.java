package ru.urfu.cache;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.mathmechbot.UserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.user.UserCacheStorage;


/**
 * Тесты внутреннего кэша UserCacheStorage.
 */
public class UserCacheTest {
    private final static int MAX_SIZE = 2;
    UserCacheStorage cache = new UserCacheStorage(MAX_SIZE);
    private final static long TEST_ID = 0L;
    private final UserState testState = UserState.DEFAULT;
    private final User testMember = new User(TEST_ID, testState);
    private final static long TEST_ID_1 = 1L;
    private final UserState testState1 = UserState.REGISTRATION_NAME;
    private final User testMember1 = new User(TEST_ID_1, testState1);
    private final static long TEST_ID_2 = 2L;
    private final UserState testState2 = UserState.EDITING_ADDITIONAL_EDIT;
    private final User testMember2 = new User(TEST_ID_2, testState2);

    /**
     * Первоначальная настройка тестов.
     * В кэше находится 1 объект - testMember
     */
    @BeforeEach
    void setupTest() {
        cache.add(testMember);
    }

    /**
     * Проверка на переполнение кэша.
     * По задумке он должен полностью очищаться при переполнении.
     */
    @Test
    @DisplayName("Overflow")
    void testOverflow() {
        cache.add(testMember1);
        cache.add(testMember2);
        Assertions.assertTrue(cache.get(TEST_ID).isEmpty());
    }

    /**
     * Тестирование метода update.
     */
    @Test
    @DisplayName("Тест update")
    void testUpdate() {
        User member = new User(TEST_ID, UserState.REGISTRATION_SPECIALTY);
        cache.update(member);
        Assertions.assertEquals(Optional.of(member), cache.get(TEST_ID));
    }

    /**
     * Тестирование изменения currentState у объекта User в кэше.
     */
    @Test
    @DisplayName("Изменение состояния")
    void testChangeState() {
        User member = new User(TEST_ID, UserState.DELETION_CONFIRMATION);
        cache.changeUserState(TEST_ID, UserState.DELETION_CONFIRMATION);
        Assertions.assertEquals(Optional.of(member), cache.get(TEST_ID));
    }

    /**
     * Тестирование метода getAll(), отвечающего за получение всей информации из кэша.
     */
    @Test
    @DisplayName("get All")
    void testGetAll() {
        cache.add(testMember1);
        Assertions.assertEquals(List.of(testMember, testMember1), cache.getAll());
    }

    /**
     * Тестирование удаления пользователя из кэша.
     */
    @Test
    @DisplayName("Delete")
    void testDelete() {
        cache.delete(testMember);
        Assertions.assertTrue(cache.get(TEST_ID).isEmpty());
    }
}
