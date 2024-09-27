package ru.urfu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

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


    /**
     * Проверяем, что на сообщение /help логика отправляет справку
     */
    @Test
    @DisplayName("Проверка команды /help")
    void helpCommandTest() {
        final Message request = new MessageBuilder().text("/help").build();
        final Message response = new EchoBotCore().processMessage(request, 0L, new DummyBot());
        Assertions.assertEquals(HELP_MESSAGE_TEXT, response.text());
    }


    /**
     * Проверяем, что на сообщение /start логика отправляет справку
     */
    @Test
    @DisplayName("Проверка команды /start")
    void startCommandTest() {
        final Message request = new MessageBuilder().text("/start").build();
        final Message response = new EchoBotCore().processMessage(request, 0L, new DummyBot());
        Assertions.assertEquals(HELP_MESSAGE_TEXT, response.text());
    }


    /**
     * Проверяем, что логика дублирует набор слов с префиксом "Ты написал: "
     */
    @Test
    @DisplayName("Набор слов")
    void someTextTest() {
        final String someText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        final Message request = new MessageBuilder().text(someText).build();
        final Message response = new EchoBotCore().processMessage(request, 0L, new DummyBot());
        Assertions.assertEquals("Ты написал: " + someText, response.text());
    }


    /**
     * Проверяем, что на сообщение без текста логика отвечает ничем (вместо Message -- null)
     */
    @Test
    @DisplayName("Сообщение без текста")
    void textIsNull() {
        final Message request = new MessageBuilder().build();
        final Message response = new EchoBotCore().processMessage(request, 0L, new DummyBot());
        Assertions.assertNull(response);
    }
}
