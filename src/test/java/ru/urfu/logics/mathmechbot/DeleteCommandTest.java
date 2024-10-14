package ru.urfu.logics.mathmechbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.UserBuilder;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для команды удаления
 */
@DisplayName("[/delete] Состояние: ожидание подтверждения")
final class DeleteCommandTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики и ложного бота для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);
    }

    /**
     * <p>Проверяем, что у зарегистрированного пользователя при просьбе
     * и подтверждении удалить данные, данные удаляются.</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Проверяем, что бот отправил запрос на подтверждение.</li>
     *     <li>Нажимаем кнопку "Да".</li>
     *     <li>Проверяем, что бот подтвердил удаление данных.</li>
     *     <li>Проверяем, что пользователь вернулся в дефолтное состояние.</li>
     *     <li>Проверяем, что данные действительно удалились.</li>
     *     <li>Проверяем, что пользователю отправилась справка.</li>
     * </ol>
     */
    @Test
    @DisplayName("Нажата кнопка 'Да' во время подтверждения удаления данных")
    void testRegisteredUserSaysYes() {
        utils.registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DELETE_MESSAGE));
        Assertions.assertEquals(
                new LocalMessageBuilder().text("""
                                Точно удаляем?

                                ФИО: Иванов Иван Иванович
                                Группа: ММП-102 (МЕН-123456)""")
                        .buttons(TestConstants.YES_NO_BACK)
                        .build(),
                bot.getOutcomingMessageList().getFirst());

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.ACCEPT_MESSAGE));
        Assertions.assertEquals(
                new LocalMessageBuilder().text("Удаляем...").build(),
                bot.getOutcomingMessageList().get(1));

        Assertions.assertTrue(storage.userEntries.get(0L).isEmpty());
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(2));
    }

    /**
     * <p>Проверяем случай, когда зарегистрированный пользователь
     * запускает команду /delete, но не подтверждает удаление.</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Нажимаем кнопку "Неа".</li>
     *     <li>Проверяем, что бот подтвердил не удаление данных.</li>
     *     <li>Проверяем, что пользователь вернулся в дефолтное состояние.</li>
     *     <li>Проверяем, что данные не удалились.</li>
     *     <li>Проверяем, что пользователю отправилась справка.</li>
     * </ol>
     */
    @Test
    @DisplayName("Нажата кнопка 'Неа' во время подтверждения удаления данных")
    void testRegisteredUserSaysNo() {
        utils.registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
        final UserEntry userEntryBeforeDelete = storage.userEntries.get(0L).orElseThrow();

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DELETE_MESSAGE));
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DECLINE_MESSAGE));
        Assertions.assertEquals(
                new LocalMessageBuilder().text("Отмена...").build(),
                bot.getOutcomingMessageList().get(1));

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                userEntryBeforeDelete,
                storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(2));
    }

    /**
     * <p>Проверяем случай, когда зарегистрированный пользователь
     * запускает команду /delete, а затем нажимает кнопку "Назад".</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Нажимаем кнопку "Назад".</li>
     *     <li>Проверяем, что бот вернулся в основное состояние.</li>
     *     <li>Проверяем, что данные не удалились.</li>
     *     <li>Проверяем, что пользователю отправилась справка.</li>
     * </ol>
     */
    @Test
    @DisplayName("Нажата кнопка 'Назад' во время подтверждения удаления данных")
    void testRegisteredUserSaysBack() {
        utils.registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
        final UserEntry userEntryBeforeDelete = storage.userEntries.get(0L).orElseThrow();

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DELETE_MESSAGE));
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                userEntryBeforeDelete,
                storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(1));
    }

    /**
     * <p>Проверяем случай, когда зарегистрированный пользователь
     * запускает команду /delete, но на запрос о подтверждении
     * удаления пользователь отправляет непредвиденный текст.</p>
     *
     * <ol>
     *     <li>Регистрируемся.</li>
     *     <li>Запускаем команду <code>/delete</code>.</li>
     *     <li>Нажимаем кнопку отправляем сообщение, не содержащее ожидаемые ответы.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что данные не удалились.</li>
     *     <li>Проверяем, что бот запросил повторный ввод.</li>
     * </ol>
     */
    @Test
    @DisplayName("Вместо подтверждения пришёл неожиданный текст")
    void testRegisteredUserSaysSomethingElse() {
        utils.registerUser(0L, "Иванов Иван Иванович", 1, "ММП", 2, "МЕН-123456");
        final UserEntry userEntryBeforeDelete = storage.userEntries.get(0L).orElseThrow();

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DELETE_MESSAGE));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Some string").build()));

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DELETION_CONFIRMATION).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                userEntryBeforeDelete,
                storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().get(1));
        Assertions.assertEquals(
                new LocalMessageBuilder().text("""
                                Точно удаляем?

                                ФИО: Иванов Иван Иванович
                                Группа: ММП-102 (МЕН-123456)""")
                        .buttons(TestConstants.YES_NO_BACK)
                        .build(),
                bot.getOutcomingMessageList().get(2));
    }
}
