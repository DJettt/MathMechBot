package ru.urfu.logics.mathmechbot.registration;

import java.util.Objects;
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
 * Тесты для команды регистрации в состоянии запроса года обучения.
 */
@DisplayName("[/register] Состояние: ожидание года обучения")
final class YearStateTest {
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
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Иванов Артём Иванович").build()));

        currentUser = storage.users.get(0L).orElseThrow();
        currentUserEntry = storage.userEntries.get(0L).orElseThrow();

        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что бот принимает годы обучения.</p>
     *
     * <ol>
     *     <li>Отправляем корректное корректный год обучения.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние:
     *          <ul>
     *              <li>Если год первый, то в состояние выбора направления первого курса.</li>
     *              <li>Если иной, то в состояние выбора направления старших курсов.</li>
     *          </ul>
     *     </li>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользовательские данные теперь содержат год обучения.</li>
     * </ol>
     *
     * @param year корректный год обучения.
     */
    @DisplayName("Корректный ввод")
    @ValueSource(strings = {"1", "2", "3", "4", "5", "6"})
    @ParameterizedTest(name = "\"{0}\" - сообщение, содержащее корректный год обучения")
    void testCorrectData(String year) {
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(year).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_SPECIALTY).build(),
                storage.users.get(0L).orElseThrow());

        if (Objects.equals(year, "1")) {
            Assertions.assertEquals(
                    RegistrationConstants.ASK_FIRST_YEAR_SPECIALTY,
                    bot.getOutcomingMessageList().getFirst());
        } else {
            Assertions.assertEquals(
                    RegistrationConstants.ASK_LATER_YEAR_SPECIALTY,
                    bot.getOutcomingMessageList().getFirst());
        }
        Assertions.assertEquals(
                new UserEntryBuilder(currentUserEntry).year(Integer.parseInt(year)).build(),
                storage.userEntries.get(0L).orElseThrow());
    }

    /**
     * <p>Проверяем, что бот не принимает иных.</p>
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
    @ParameterizedTest(name = "\"{0}\" не является годом обучения")
    void testIncorrectData(String text) {
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder().text(text).build()));

        Assertions.assertEquals(currentUser, storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(currentUserEntry, storage.userEntries.get(0L).orElseThrow());

        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(RegistrationConstants.ASK_YEAR, bot.getOutcomingMessageList().getLast());
    }

    /**
     * <p>Проверяем, что бот корректно возвращает на шаг назад, то есть в состояние запроса ФИО.</p>
     *
     * <ol>
     *     <li>Нажимаем кнопку "Назад".</li>
     *     <li>Проверяем, что бот в вернулся в состояние запроса ФИО (запросил ФИО снова).</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBackCommand() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_NAME).build(),
                storage.users.get(0L).orElseThrow());
    }
}
