package ru.urfu.logics.mathmechbot.registration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.TestConstants;
import ru.urfu.logics.mathmechbot.TestUtils;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.models.UserEntryBuilder;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;

/**
 * Тесты для команды регистрации в состоянии запроса номера группы.
 */
@DisplayName("[/register] Состояние: ожидание номера группы")
final class GroupTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    private User currentUser;
    private UserEntry currentUserEntry;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста, выполняем все предыдущие шаги регистрации.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage();
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        logic.processMessage(utils.makeRequestFromMessage(new TestConstants().registerMessage));
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
                new User(0L, MathMechBotUserState.REGISTRATION_MEN),
                storage.getUsers().get(0L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentUserEntry).group(Integer.parseInt(group)).build(),
                storage.getUserEntries().get(0L).orElseThrow());
        Assertions.assertEquals(new RegistrationConstants().askMen, bot.getOutcomingMessageList().getLast());
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
        Assertions.assertEquals(new TestConstants().tryAgain, bot.getOutcomingMessageList().getFirst());
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
        logic.processMessage(utils.makeRequestFromMessage(new TestConstants().backMessage));
        Assertions.assertEquals(
                new User(0L, MathMechBotUserState.REGISTRATION_SPECIALTY),
                storage.getUsers().get(0L).orElseThrow());
    }
}
