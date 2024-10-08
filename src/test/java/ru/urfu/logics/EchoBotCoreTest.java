package ru.urfu.logics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.bots.DummyBot;
import ru.urfu.localobjects.LocalMessageBuilder;

/**
 * Тесты для класса EchoBotCore
 */
public final class EchoBotCoreTest {
    final static String HELP_MESSAGE_TEXT = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                Пассивная способность: Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало \
                твоего сообщения!\n
                /help - Показать доступные команды.
                /start - Начинает диалог с начала. (нет)
                Приятного использования!""";

    private DummyBot bot;
    private EchoBotCore logic;

    /**
     * Создаём объект логики и ложного бота для каждого теста.
     */
    @BeforeEach
    public void setupTest() {
        bot = new DummyBot();
        logic = new EchoBotCore();
    }

    /**
     * Проверяем, что на сообщение /help логика отправляет справку
     */
    @Test
    @DisplayName("Проверка команды /help")
    void testHelpCommand() {
        final LocalMessage request = new LocalMessageBuilder().text("/help").build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertEquals(HELP_MESSAGE_TEXT, bot.getOutcomingMessageList().getLast().text());
    }

    /**
     * Проверяем, что на сообщение /start логика отправляет справку
     */
    @Test
    @DisplayName("Проверка команды /start")
    void testStartCommand() {
        final LocalMessage request = new LocalMessageBuilder().text("/start").build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertEquals(HELP_MESSAGE_TEXT, bot.getOutcomingMessageList().getLast().text());
    }

    /**
     * Проверяем, что логика дублирует набор слов с префиксом "Ты написал: "
     */
    @Test
    @DisplayName("Набор слов")
    void testSomeText() {
        final String someText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        final LocalMessage request = new LocalMessageBuilder().text(someText).build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertEquals("Ты написал: " + someText, bot.getOutcomingMessageList().getLast().text());
    }

    /**
     * Проверяем, что на сообщение без текста логика отвечает ничем (вместо LocalMessage -- null)
     */
    @Test
    @DisplayName("Сообщение без текста")
    void testNullText() {
        final LocalMessage request = new LocalMessageBuilder().build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertTrue(bot.getOutcomingMessageList().isEmpty());
    }
}
