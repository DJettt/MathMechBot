package ru.urfu.logics.mathmechbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import ru.urfu.logics.DummyBot;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;


/**
 * Тесты для класса MathMechBotCore
 */
@DisplayName("Логика матмех бота")
final class MathMechBotCoreTest {
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
}
