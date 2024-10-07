package ru.urfu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.urfu.logics.EchoBotCore;
import ru.urfu.logics.LogicCore;

/**
 * Тесты логики эхо-бота
 */
public final class EchoBotCoreTest {
    final static String HELP_MESSAGE_TEXT = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";

    private DummyBot bot;
    private LogicCore logic;

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
    void helpCommandTest() {
        final Message request = new MessageBuilder().text("/help").build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertEquals(HELP_MESSAGE_TEXT, bot.getOutcomingMessageList().getLast().text());
    }


    /**
     * Проверяем, что на сообщение /start логика отправляет справку
     */
    @Test
    @DisplayName("Проверка команды /start")
    void startCommandTest() {
        final Message request = new MessageBuilder().text("/start").build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertEquals(HELP_MESSAGE_TEXT, bot.getOutcomingMessageList().getLast().text());
    }


    /**
     * Проверяем, что логика дублирует набор слов с префиксом "Ты написал: "
     */
    @Test
    @DisplayName("Набор слов")
    void someTextTest() {
        final String someText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        final Message request = new MessageBuilder().text(someText).build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertEquals("Ты написал: " + someText, bot.getOutcomingMessageList().getLast().text());
    }


    /**
     * Проверяем, что на сообщение без текста логика отвечает ничем (вместо Message -- null)
     */
    @Test
    @DisplayName("Сообщение без текста")
    void textIsNull() {
        final Message request = new MessageBuilder().build();
        logic.processMessage(request, 0L, bot);
        Assertions.assertTrue(bot.getOutcomingMessageList().isEmpty());
    }
}
