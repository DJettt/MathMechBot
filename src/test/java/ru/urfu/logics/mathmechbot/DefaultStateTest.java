package ru.urfu.logics.mathmechbot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;

@DisplayName("[default] Состояние: по умолчанию")
final class DefaultStateTest {
    private TestUtils utils;
    private MathMechStorage storage;
    private MathMechBotCore logic;
    private DummyBot bot;

    /**
     * Создаём объект логики, ложного бота и утилиты для каждого теста.
     */
    @BeforeEach
    void setupTest() {
        storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        logic = new MathMechBotCore(storage);
        bot = new DummyBot();
        utils = new TestUtils(logic, bot);
    }

    @DisplayName("Тестирование команды /info")
    @ValueSource(strings = {"Ильин Илья Ильич", "Ильин Илья", "Аа Аа"})
    @ParameterizedTest(name = "\"{0}\" - различные конфигурации ФИО")
    void testInfoExist(String fullName) {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.INFO_MESSAGE));
        Assertions.assertEquals(TestConstants.ASK_FOR_REGISTRATION_MESSAGE, bot.getOutcomingMessageList().getFirst());

        utils.registerUser(0L, fullName, 2, "КН", 3, "МЕН-654321");
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.INFO_MESSAGE));
        Assertions.assertEquals(new LocalMessageBuilder()
                        .text("""
                                Данные о Вас:

                                ФИО: %s
                                Группа: КН-203 (МЕН-654321)"""
                                .formatted(fullName))
                        .build(),
                bot.getOutcomingMessageList().getFirst());
    }

    /**
     * Проверяем, что на попытку незарегистрированного пользователя
     * удалить свои данные, ядро отвечает просьбой о регистрации.
     */
    @Test
    @DisplayName("Незарегистрированный пользователь пытается удалить свои данные")
    void testUnregisteredUser() {
        logic.processMessage(utils.makeRequestFromMessage(TestConstants.DELETE_MESSAGE));
        Assertions.assertEquals(TestConstants.ASK_FOR_REGISTRATION_MESSAGE, bot.getOutcomingMessageList().getLast());
    }
}
