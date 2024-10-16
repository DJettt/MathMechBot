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
 * Тесты для команды регистрации в состояниях запроса направления подготовки.
 */
@DisplayName("[/register] Состояние: ожидание направления подготовки")
final class SpecialtyTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    User currentFirstYearUser;
    UserEntry currentFirstYearUserEntry;
    User currentSecondYearUser;
    UserEntry currentSecondYearUserEntry;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста,
     * выполняем все предыдущие шаги регистрации для первокурсника и второкурсника.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Иванов Иван Иванович").build()));
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder().text("1").build()));
        currentFirstYearUser = storage.users.get(0L).orElseThrow();
        currentFirstYearUserEntry = storage.userEntries.get(0L).orElseThrow();

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.REGISTER_MESSAGE, 1));
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text("Ильин Илья Ильич").build(), 1));
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder().text("2").build(), 1));
        currentSecondYearUser = storage.users.get(1L).orElseThrow();
        currentSecondYearUserEntry = storage.userEntries.get(1L).orElseThrow();

        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что бот принимает общие направления и для первокурсника, и для второкурсника.</p>
     *
     * <ol>
     *     <li>Отправляем от первокурсника корректную аббревиатуру направления подготовки.</li>
     *     <li>Проверяем, что состояние студента изменилось на запрос группы.</li>
     *     <li>Проверяем, что запись студента содержит указанное направление.</li>
     *     <li>Проверяем, что бот отправил сообщение с запросом группы.</li>
     *     <li>Аналогично для второкурсника.</li>
     * </ol>
     *
     * @param specialtyAbbreviation корректная аббревиатура направления подготовки, общего для всех курсов.
     */
    @DisplayName("Корректный ввод для всех курсов")
    @ValueSource(strings = {"КБ", "ФТ"})
    @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки для обоих курсов")
    void testCommonData(String specialtyAbbreviation) {
        // Первокурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentFirstYearUserEntry).specialty(specialtyAbbreviation).build(),
                storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(RegistrationConstants.ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());

        // Второкурсник
        logic.processMessage(utils.makeRequestFromMessage(
                new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1));
        Assertions.assertEquals(
                new UserBuilder(1L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                storage.users.get(1L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentSecondYearUserEntry).specialty(specialtyAbbreviation).build(),
                storage.userEntries.get(1L).orElseThrow());
        Assertions.assertEquals(RegistrationConstants.ASK_GROUP_NUMBER, bot.getOutcomingMessageList().get(1));
    }

    /**
     * <p>Проверяем, что бот принимает направления подготовки первого курса только у первокурсников.</p>
     *
     * <ol>
     *     <li>Отправляем от старшекурсника корректную аббревиатуру направления подготовки первого курса.</li>
     *     <li>Проверяем, что состояние пользователя изменилось.</li>
     *     <li>Проверяем, что введённые данные сохранились.</li>
     *     <li>Проверяем, что бот отправил запрос номера группы.</li>
     *
     *     <li>Отправляем ту же аббревиатуру от второкурсника.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что введённые данные не изменились.</li>
     *     <li>Проверяем, что бот запрос повторный ввод.</li>
     * </ol>
     *
     * @param specialtyAbbreviation корректная аббревиатура направления подготовки только первого курса.
     */
    @DisplayName("Корректный ввод для первого курса")
    @ValueSource(strings = {"КНМО", "ММП"})
    @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки только для первого курса")
    void testFirstYearOnlyData(String specialtyAbbreviation) {
        // Первокурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 0L));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentFirstYearUserEntry).specialty(specialtyAbbreviation).build(),
                storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(RegistrationConstants.ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
        bot.getOutcomingMessageList().clear();

        // Второкурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1));
        Assertions.assertEquals(currentSecondYearUser, storage.users.get(1L).orElseThrow());
        Assertions.assertEquals(currentSecondYearUserEntry, storage.userEntries.get(1L).orElseThrow());
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(RegistrationConstants.ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
    }

    /**
     * <p>Проверяем, что бот принимает направления подготовки старших курсов только у старшекурсников.</p>
     *
     * <ol>
     *     <li>Отправляем от первокурсника корректную аббревиатуру направления подготовки старших курсов.</li>
     *     <li>Проверяем, что состояние пользователя не изменилось.</li>
     *     <li>Проверяем, что введённые данные не изменились.</li>
     *     <li>Проверяем, что бот запрос повторный ввод.</li>
     *
     *     <li>Отправляем ту же аббревиатуру от второкурсника.</li>
     *     <li>Проверяем, что состояние пользователя изменилось.</li>
     *     <li>Проверяем, что введённые данные сохранились.</li>
     *     <li>Проверяем, что бот отправил запрос номера группы.</li>
     * </ol>
     *
     * @param specialtyAbbreviation корректная аббревиатура направления подготовки только старших курсов.
     */
    @DisplayName("Корректный ввод для поздних курса")
    @ValueSource(strings = {"КН", "МО", "МХ", "ПМ"})
    @ParameterizedTest(name = "\"{0}\" - корректное направление подготовки только для поздних курса")
    void testLaterYearsOnlyData(String specialtyAbbreviation) {
        // Первокурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build()));
        Assertions.assertEquals(currentFirstYearUser, storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(currentFirstYearUserEntry, storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(RegistrationConstants.ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
        bot.getOutcomingMessageList().clear();

        // Второкурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(specialtyAbbreviation).build(), 1));
        Assertions.assertEquals(
                new UserBuilder(1L, MathMechBotUserState.REGISTRATION_GROUP).build(),
                storage.users.get(1L).orElseThrow());
        Assertions.assertEquals(
                new UserEntryBuilder(currentSecondYearUserEntry).specialty(specialtyAbbreviation).build(),
                storage.userEntries.get(1L).orElseThrow());
        Assertions.assertEquals(RegistrationConstants.ASK_GROUP_NUMBER, bot.getOutcomingMessageList().getFirst());
        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что бот не принимает ни от кого неверные строки.</p>
     *
     * <ol>
     *     <li>Отправляем сообщение, не содержащее разрешённого направления подготовки.</li>
     *     <li>Проверяем, что состояние первокурсника не изменилось.</li>
     *     <li>Проверяем, что запись первокурсника не изменилось.</li>
     *     <li>Аналогично проверяем для второкурсника.</li>
     * </ol>
     *
     * @param incorrectMessageText не разрешённое направление подготовки.
     */
    @DisplayName("Некорректный ввод")
    @NullAndEmptySource
    @ValueSource(strings = {" КН", "КН ", " КН ", "кн", "string", "0"})
    @ParameterizedTest(name = "\"{0}\" не является разрешённым направление подготовки")
    void testIncorrectData(String incorrectMessageText) {
        // Первокурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(incorrectMessageText).build()));
        Assertions.assertEquals(currentFirstYearUser, storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(currentFirstYearUserEntry, storage.userEntries.get(0L).orElseThrow());
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(RegistrationConstants.ASK_FIRST_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
        bot.getOutcomingMessageList().clear();

        // Второкурсник
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(incorrectMessageText).build(), 1));
        Assertions.assertEquals(currentSecondYearUser, storage.users.get(1L).orElseThrow());
        Assertions.assertEquals(currentSecondYearUserEntry, storage.userEntries.get(1L).orElseThrow());
        Assertions.assertEquals(TestConstants.TRY_AGAIN, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(RegistrationConstants.ASK_LATER_YEAR_SPECIALTY, bot.getOutcomingMessageList().get(1));
    }

    /**
     * <p>Проверяем, что бот корректно возвращает на шаг назад, то есть в состояние запроса года обучения.</p>
     *
     * <ol>
     *     <li>Нажимаем кнопку "Назад"</li>
     *     <li>Проверяем, что для обоих пользователей состояние изменилось на запрос года обучения.</li>
     *     <li>Проверяем, что бот вновь спросил год обучения.</li>
     * </ol>
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBackCommand() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE));
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.BACK_MESSAGE, 1));

        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                storage.users.get(0L).orElseThrow());
        Assertions.assertEquals(
                new UserBuilder(1L, MathMechBotUserState.REGISTRATION_YEAR).build(),
                storage.users.get(1L).orElseThrow());

        Assertions.assertEquals(RegistrationConstants.ASK_YEAR, bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(RegistrationConstants.ASK_YEAR, bot.getOutcomingMessageList().get(1));
    }
}
