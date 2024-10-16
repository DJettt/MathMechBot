package ru.urfu.logics.mathmechbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.mathmechbot.MathMechBotCore;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Тесты для дефолтного состояния.
 */
@DisplayName("[default] Состояние: по умолчанию")
final class DefaultStateTest {
    private TestUtils utils;
    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        final MathMechStorage storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);
    }

    /**
     * <p>Проверяем, что на попытку незарегистрированного пользователя
     * удалить или просмотреть свои данные, ядро отвечает просьбой о регистрации.</p>
     *
     * <ol>
     *     <li>Отправляем команду.</li>
     *     <li>Проверяем, что бот попросил зарегистрироваться.</li>
     * </ol>
     * @param command тестируемая команда
     */
    @DisplayName("Незарегистрированный пользователь вызывает команду для зарегистрированных пользователей")
    @ValueSource(strings = {TestConstants.INFO_COMMAND, TestConstants.DELETE_COMMAND})
    @ParameterizedTest(name = "{0} - команда, недоступная незарегистрированному пользователю")
    void testUnregisteredUser(String command) {
        logic.processMessage(utils.makeRequestFromMessage(new LocalMessageBuilder().text(command).build()));
        Assertions.assertEquals(TestConstants.ASK_FOR_REGISTRATION, bot.getOutcomingMessageList().getLast());
    }

    /**
     * <p>Проверяем, что бот корректно выводит данные пользователей.</p>
     *
     * <p>Проверяем человека с отчество и без.</p>
     *
     * @param fullName ФИО
     */
    @DisplayName("Тестирование команды " + TestConstants.INFO_COMMAND)
    @ValueSource(strings = {"Ильин Илья Ильич", "Ильин Илья"})
    @ParameterizedTest(name = "\"{0}\" - различные конфигурации ФИО")
    void testInfoExist(String fullName) {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.INFO_MESSAGE));
        Assertions.assertEquals(TestConstants.ASK_FOR_REGISTRATION, bot.getOutcomingMessageList().getFirst());

        utils.registerUser(0L, fullName, 2, "КН", 2, "МЕН-654321");
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.INFO_MESSAGE));
        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Данные о Вас:

                                ФИО: %s
                                Группа: КН-202 (МЕН-654321)"""
                                .formatted(fullName))
                        .build(),
                bot.getOutcomingMessageList().getFirst());
    }
}
