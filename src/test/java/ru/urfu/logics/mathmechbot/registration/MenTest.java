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
import ru.urfu.mathmechbot.models.UserEntryBuilder;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для команды регистрации в состоянии запроса группы в формате МЕН.
 */
@DisplayName("[/register] Состояние: ожидание академической группы в формате МЕН")
final class MenTest {
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
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Максимов Андрей").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("2").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("ФТ").build()));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("5").build()));

        currentUser = storage.users.get(0L).orElseThrow();
        currentUserEntry = storage.userEntries.get(0L).orElseThrow();

        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что бот принимает корректные группы в формате МЕН.</p>
     *
     * <ol>
     *     <li>Отправляем корректный номер МЕН-группы.</li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     *     <li>Проверяем, что пользовательские данные теперь содержат номер группы.</li>
     * </ol>
     *
     * @param men сообщение с корректным номером МЕН-группы.
     */
    @DisplayName("Корректный ввод")
    @ValueSource(strings = {
            "МЕН-123456", "МЕН-000000", "МЕН-111111", "МЕН-999999",
            "   МЕН-123456", "МЕН-123456   ", "   МЕН-123456   "
    })
    @ParameterizedTest(name = "\"{0}\" - сообщение, содержащее корректную МЕН-группу")
    void testCorrectData(String men) {
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(men).build()));

        Assertions.assertEquals(
                new LocalMessageBuilder()
                        .text(RegistrationConstants.CONFIRMATION_PREFIX
                                + new UserEntryBuilder(currentUserEntry).men(men.trim()).build().toHumanReadable())
                        .buttons(TestConstants.YES_NO_BACK)
                        .build(),
                bot.getOutcomingMessageList().getLast());

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_CONFIRMATION).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentUserEntry).men(men.trim()).build(),
                storage.userEntries.get(0L).orElseThrow());
    }

    /**
     * <p>Проверяем, что бот не принимает некорректные сообщения.</p>
     *
     * <ol>
     *     <li>Отправляем некорректные данные.</li>
     *     <li>Проверяем, что запросил повторный ввод.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что запись пользователя не изменилась.</li>
     * </ol>
     *
     * @param text некорректное сообщение.
     */
    @DisplayName("Некорректный ввод")
    @NullAndEmptySource
    @ValueSource(strings = {"мен-123456", "Мен-123456", "МЕН--123456", "МЕН-1234567", "МЕН-12345"})
    @ParameterizedTest(name = "\"{0}\" не является номером МЕН-группы")
    void testIncorrectData(String text) {
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(text).build()));

        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(currentUser, storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(currentUserEntry, storage.userEntries.get(0L).orElseThrow());
    }

    /**
     * <p>Проверяем, что бот корректно возвращает на шаг назад,
     * то есть в состояние запроса номера группы.</p>
     *
     * <ol>
     *     <li>Нажимаем кнопку "Назад".</li>
     *     <li>Проверяем, что бот в вернулся в состояние запроса номера группы.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBackCommand() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                storage.users.get(0L).orElseThrow());
    }
}
