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
        final LocalMessage response = new EchoBotCore().processMessage(new LocalMessage("/help"));
        Assertions.assertEquals(HELP_MESSAGE_TEXT, response.getText());
    }


    /**
     * Проверяем, что на сообщение /start логика отправляет справку
     */
    @Test
    @DisplayName("Проверка команды /start")
    void startCommandTest() {
        final LocalMessage response = new EchoBotCore().processMessage(new LocalMessage("/start"));
        Assertions.assertEquals(HELP_MESSAGE_TEXT, response.getText());
    }

    /**
     * Проверяем, что логика дублирует набор слов с префиксом "Ты написал: "
     */
    @Test
    @DisplayName("Набор слов")
    void someTextTest() {
        final String someText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        final LocalMessage response = new EchoBotCore().processMessage(new LocalMessage(someText));
        Assertions.assertEquals("Ты написал: " + someText, response.getText());
    }

    /**
     * Проверяем, что на сообщение без текста логика отвечает ничем (вместо Message -- null)
     */
    @Test
    @DisplayName("Сообщение без текста")
    void textIsNull() {
        final LocalMessage response = new EchoBotCore().processMessage(new LocalMessage(null));
        Assertions.assertNull(response);
    }
}
