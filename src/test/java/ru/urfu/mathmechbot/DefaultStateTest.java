package ru.urfu.mathmechbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;

/**
 * Тесты для дефолтного состояния.
 */
@DisplayName("Состояние по умолчанию")
final class DefaultStateTest {
    private final static String INFO_COMMAND = "/info";

    private final LocalMessage askForRegistration =
            new LocalMessage("Сперва нужно зарегистрироваться.");

    private TestUtils utils;
    private DummyBot bot;

    /**
     * <p>Создаём объект логики, ложного бота и утилиты для каждого теста.</p>
     */
    @BeforeEach
    void setupTest() {
        final MMBCore logic = new MMBCore(new MathMechArrayStorage());
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
    @DisplayName("Команды, недоступные незарегистрированному пользователю")
    @ValueSource(strings = {INFO_COMMAND, "/delete"})
    @ParameterizedTest(name = "{0} - команда, недоступная незарегистрированному пользователю")
    void testUnregisteredUser(final String command) {
        utils.sendMessageToLogic(new LocalMessage(command));
        Assertions.assertEquals(askForRegistration,
                bot.getOutcomingMessageList().getFirst());
    }

    /**
     * <p>Проверяем, что бот корректно выводит данные пользователей.</p>
     *
     * <p>Проверяем человека с отчество и без.</p>
     *
     * @param fullName ФИО
     */
    @DisplayName("Тестирование команды /info")
    @ValueSource(strings = {"Ильин Илья Ильич", "Ильин Илья"})
    @ParameterizedTest(name = "\"{0}\" - различные конфигурации ФИО")
    void testInfoExist(String fullName) {
        utils.sendMessageToLogic(new LocalMessage(INFO_COMMAND));
        Assertions.assertEquals(askForRegistration,
                bot.getOutcomingMessageList().getFirst());

        utils.registerUser(0L, fullName, 2,
                "КН", 2, "МЕН-654321");
        utils.sendMessageToLogic(new LocalMessage(INFO_COMMAND));

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
