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
import ru.urfu.logics.mathmechbot.TestConstants;
import ru.urfu.logics.mathmechbot.TestUtils;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.models.MathMechBotUserState;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserBuilder;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для состояния ожидания подтверждения регистрационных данных.
 */
@DisplayName("[/register] Состояние: ожидание подтверждения")
final class ConfirmationTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    User currentUser;
    UserEntry currentUserEntry;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста, выполняем все предыдущие шаги регистрации.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Денисов Денис Денисович").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("4").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("КБ").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("2").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("МЕН-162534").build()));

        currentUser = storage.users.get(0L).orElseThrow();
        currentUserEntry = storage.userEntries.get(0L).orElseThrow();

        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что при нажатии 'Да' данные сохраняются.</p>
     *
     * <ol>
     *     <li>Отправляем значение кнопки 'Да'.</li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     *     <li>Проверяем, что пользовательские данные те же.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Нет'")
    void testYes() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.ACCEPT_MESSAGE));

        Assertions.assertEquals(
                new LocalMessageBuilder().text("Сохранил...").build(),
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(1));

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(currentUserEntry, storage.userEntries.get(0L).orElseThrow());
    }

    /**
     * <p>Проверяем, что при нажатии 'Нет' данные стираются.</p>
     *
     * <ol>
     *     <li>Отправляем значение кнопки 'Нет'.</li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     *     <li>Проверяем, что пользовательские данные стёрты.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Нет'")
    void testNo() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DECLINE_MESSAGE));

        Assertions.assertEquals(
                new LocalMessageBuilder().text("Отмена...").build(),
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(1));

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertTrue(storage.userEntries.get(0L).isEmpty());
    }

    /**
     * <p>Проверяем случай, когда пользователь нажимает кнопку "Назад".</p>
     *
     * <ol>
     *     <li>Отправляем значение кнопки 'Назад'.</li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBack() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));
        Assertions.assertEquals(
                RegistrationConstants.ASK_MEN,
                bot.getOutcomingMessageList().getFirst()
        );
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_MEN).build(),
                storage.users.get(0L).orElseThrow());
    }

    /**
     * <p>Проверяем случай, когда пользователь отправляет что-то другое.</p>
     *
     * <ol>
     *     <li>Отправляем какой-то текст.</li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     * </ol>
     *
     * @param text неожиданные данные
     */
    @DisplayName("Не кнопка")
    @NullAndEmptySource
    @ValueSource(strings = {"SomeString"})
    @ParameterizedTest(name = "\"{0}\" - не корректный ввод")
    void testSomethingElse(String text) {
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder().text(text).build()));
        Assertions.assertEquals(
                TestConstants.TRY_AGAIN,
                bot.getOutcomingMessageList().getFirst()
        );
        Assertions.assertEquals(currentUser, storage.users.get(0L).orElseThrow());
    }
}
