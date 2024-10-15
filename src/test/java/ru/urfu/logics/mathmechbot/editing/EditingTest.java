package ru.urfu.logics.mathmechbot.editing;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.TestConstants;
import ru.urfu.logics.mathmechbot.TestUtils;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.models.User;
import ru.urfu.logics.mathmechbot.models.UserBuilder;
import ru.urfu.logics.mathmechbot.models.UserEntry;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для команды регистрации в состоянии изменения информации о себе.
 */
@DisplayName("[/register] Состояние: ожидание ФИО")
final class EditingTest {
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
        logic.processMessage(
                utils.makeRequestFromMessage(new LocalMessageBuilder().text(TestConstants.ACCEPT_COMMAND).build()));

        currentUser = storage.getUsers().get(0L).orElseThrow();
        currentUserEntry = storage.getUserEntries().get(0L).orElseThrow();

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
    @SuppressWarnings("MagicNumber")
    @DisplayName("Команда '/edit' ")
    void testEdit() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.EDIT_MESSAGE));

        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().getFirst());
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDIT_COMMAND)
                .build()));
        Assertions.assertEquals(
                new LocalMessageBuilder().text("Попробуйте снова.").build(),
                bot.getOutcomingMessageList().get(1));


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_FULL_NAME_COMMAND).build()));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.EDITING_FULL_NAME).build(),
                storage.getUsers().get(0L).orElseThrow());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.BACK_COMMAND).build()));
        Assertions.assertEquals(
                TestConstants.EDITING_CHOOSE_MESSAGE,
                bot.getOutcomingMessageList().get(3));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.EDITING_CHOOSE).build(),
                storage.getUsers().get(0L).orElseThrow());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.EDITING_GROUP_COMMAND)
                .build()));
        Assertions.assertEquals(
                TestConstants.EDITING_GROUP_MESSAGE,
                bot.getOutcomingMessageList().get(4));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.EDITING_GROUP).build(),
                storage.getUsers().get(0L).orElseThrow());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text("1")
                .build()));
        Assertions.assertEquals(
                new LocalMessageBuilder()
                        .text("Хотите изменить что-нибудь еще?")
                        .buttons(new ArrayList<>(List.of(
                                Constants.YES_BUTTON,
                                Constants.NO_BUTTON)))
                        .build(),
                bot.getOutcomingMessageList().get(5));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.EDITING_ADDITIONAL_EDIT).build(),
                storage.getUsers().get(0L).orElseThrow());


        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder()
                .text(TestConstants.DECLINE_COMMAND)
                .build()));
        Assertions.assertEquals(TestConstants.HELP, bot.getOutcomingMessageList().get(8));
        Assertions.assertEquals(
                new UserBuilder(0L, MathMechBotUserState.DEFAULT).build(),
                storage.getUsers().get(0L).orElseThrow());
    }
}
