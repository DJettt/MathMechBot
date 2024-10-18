package ru.urfu.logics.mathmechbot.editing;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.TestConstants;
import ru.urfu.logics.mathmechbot.TestUtils;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.MMBUserState;
import ru.urfu.mathmechbot.models.UserBuilder;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для команды изменения информации о себе - /edit.
 */
@DisplayName("[/edit] Состояние: Default")
final class EditingTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MMBCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста, регистрируемся.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MMBCore(storage);
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
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(TestConstants.ACCEPT_COMMAND).build()));

        bot.getOutcomingMessageList().clear();
    }

    /**
     * <p>Проверяем, что команда '/edit' меняет состояние ядра и выводит нужное сообщение.</p>
     *
     * <ol>
     *     <li>Проверяем, что бот отправил нужное сообщение.</li>
     *     <li>Проверяем, что пользователь перешёл в нужное состояние.</li>
     *     <li>Проверяем повторный запрос на изменение.</li>
     *     <li>Проверяем корректное состояние при обработке запроса на изменение ФИО.</li>
     *     <li>Проверяем команду '/back'. Новое состояние и вывод ядра.</li>
     *     <li>Проверяем корректный вывод и состояние при запросе на изменение группы.</li>
     *     <li>Проверяем сообщение на дополнительное изменение.</li>
     *     <li>Проверяем Корректный выход из состояния изменения.</li>
     * </ol>
     */
    @Test
    @DisplayName("Команда '/edit' ")
    void testEdit() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.EDIT_MESSAGE));

        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDIT_COMMAND)
                .build()));
        Assertions.assertEquals(
                new LocalMessageBuilder().text("Попробуйте снова.").build(),
                bot.getOutcomingMessageList().get(1));
    }

    /**
     * Проверка изменения ФИО.
     */
    @Test
    @DisplayName("Изменение ФИО")
    void testFullName() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.EDIT_MESSAGE));

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_FULL_NAME_COMMAND).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_FULL_NAME).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text("Иванов Иван Сергеевич")
                .build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());
    }

    /**
     * Проверка работы кнопки "назад".
     */
    @Test
    @DisplayName("Кнопка 'Назад'")
    void testBackButton() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.EDIT_MESSAGE));
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.BACK_COMMAND).build()));
        Assertions.assertEquals(
                TestConstants.HELP,
                bot.getOutcomingMessageList().get(1));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.DEFAULT).build(),
                storage.getUsers().get(0L).orElseThrow());
    }

    /**
     * Проверка изменения всей информации.
     */
    @Test
    @DisplayName("Полное изменение")
    void testFullCheck() {
        final String testSurname = "Иванов";
        final String testName = "Иван";
        final String testPatronym = "Иванович";
        final String testYear = "2";
        final String testSpeciality = "КН";
        final String testGroup = "1";
        final String testMen = "МЕН-200201";

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.EDIT_MESSAGE));
        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_FULL_NAME_COMMAND)
                .build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_FULL_NAME).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(testSurname + " " + testName + " " + testPatronym).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DECLINE_MESSAGE));
        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_YEAR_COMMAND).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_YEAR).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(testYear).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DECLINE_MESSAGE));
        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_SPECIALITY_COMMAND).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_SPECIALITY).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(testSpeciality).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DECLINE_MESSAGE));
        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_GROUP_COMMAND).build()));

        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_GROUP).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(testGroup).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DECLINE_MESSAGE));
        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_MEN_COMMAND).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_MEN).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(testMen)
                .build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());

        logic.processMessage(utils.makeRequestFromMessage(TestConstants.ACCEPT_MESSAGE));
        Assertions.assertEquals(
                new UserBuilder(0L, MMBUserState.DEFAULT).build(),
                storage.getUsers().get(0L).orElseThrow());

        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().surname(), testSurname);
        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().name(), testName);
        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().patronym(), testPatronym);
        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().year(), Integer.parseInt(testYear));
        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().specialty(), testSpeciality);
        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().group(), Integer.parseInt(testGroup));
        Assertions.assertEquals(storage.getUserEntries().get(0L).orElseThrow().men(), testMen);
    }
}
