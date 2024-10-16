package ru.urfu.logics.mathmechbot.registration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.TestConstants;
import ru.urfu.logics.mathmechbot.TestUtils;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.MMBUserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserBuilder;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для команды регистрации в состоянии запроса номера группы.
 */
@DisplayName("[/register] Состояние: ожидание номера группы")
final class GroupTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MMBCore logic;
    private DummyBot bot;

    User currentUser;
    UserEntry currentUserEntry;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста, выполняем все предыдущие шаги регистрации.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MMBCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Максимов Андрей").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("2").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("ФТ").build()));
        currentUser = storage.getUsers().get(0L).orElseThrow();
        currentUserEntry = storage.getUserEntries().get(0L).orElseThrow();
        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что бот принимает корректные номера групп.</p>
     *
     * <ol>
     *     <li>Отправляем корректный номер группы.</li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     *     <li>Проверяем, что пользовательские данные теперь содержат номер группы.</li>
     * </ol>
     *
     * @param group корректный номер группы.
     */
    @DisplayName("Корректный ввод")
    @ValueSource(strings = {"1", "2", "3", "4", "5"})
    @ParameterizedTest(name = "\"{0}\" - корректный номер группы")
    void testCorrectData(String group) {
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(group).build()));

        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.REGISTRATION_MEN).build(),
                storage.getUsers().get(0L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentUserEntry).group(Integer.parseInt(group)).build(),
                storage.getUserEntries().get(0L).orElseThrow());
        Assertions.assertEquals(RegistrationConstants.ASK_MEN, bot.getOutcomingMessageList().getLast());
    }

    /**
     * <p>Проверяем, что бот не принимает некорректные сообщения.</p>
     *
     * <ol>
     *     <li>Отправляем некорректные данные.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что запись пользователя не изменилась.</li>
     *     <li>Проверяем, что запросил повторный ввод.</li>
     * </ol>
     *
     * @param text некорректное сообщение.
     */
    @DisplayName("Некорректный ввод")
    @NullAndEmptySource
    @ValueSource(strings = {"0", "01", " 1", "2 ", " 3 ", "7", "10", "a", "((("})
    @ParameterizedTest(name = "\"{0}\" не является номером группы")
    void testIncorrectData(String text) {
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(text).build()));

        Assertions.assertEquals(currentUser, storage.getUsers().get(0L).orElseThrow());
        Assertions.assertEquals(currentUserEntry, storage.getUserEntries().get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
    }

    /**
     * <p>Проверяем, что бот корректно возвращает на шаг назад,
     * то есть в состояние запроса направления подготовки.</p>
     *
     * <ol>
     *     <li>Нажимаем кнопку "Назад".</li>
     *     <li>Проверяем, что бот в вернулся в состояние запроса направления.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBackCommand() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.REGISTRATION_SPECIALTY).build(),
                storage.getUsers().get(0L).orElseThrow());
    }
}
