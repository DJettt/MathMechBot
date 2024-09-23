package ru.urfu;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class EchoBotCoreTest {
    final String HELP_MESSAGE_TEXT = """
                Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
                 \
                Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
                В любой момент ты можешь написать команду '/help' (без кавычек) и \
                тогда я тебе напомню как со мной работать! Приятного использования!""";

    @Test
    @DisplayName("/help")
    void helpCommandTest() {
        final Message response = new EchoBotCore().processMessage(new Message("/help"));
        assertThat(response.getText()).isEqualTo(HELP_MESSAGE_TEXT);
    }

    @Test
    @DisplayName("/start")
    void startCommandTest() {
        final Message response = new EchoBotCore().processMessage(new Message("/start"));
        assertThat(response.getText()).isEqualTo(HELP_MESSAGE_TEXT);
    }

    @Test
    @DisplayName("Случайный набор слов")
    void someTextTest() {
        final String someText = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        final Message response = new EchoBotCore().processMessage(new Message(someText));
        assertThat(response.getText()).isEqualTo("Ты написал: " + someText);
    }

    @Test
    @DisplayName("Сообщение без текста")
    void textIsNull() {
        final Message response = new EchoBotCore().processMessage(new Message(null));
        assertThat(response).isEqualTo(null);
    }
}
