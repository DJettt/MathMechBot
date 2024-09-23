package ru.urfu;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Objects;


/**
 * Примитивный эхо-бот
 */
public class EchoBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final String START_MESSAGE = """
            Привет, я эхо бот! Сейчас я расскажу как ты можешь со мной взаимодействовать.
             \
            Я пишу твое сообщение тебе обратно но добавляю фразу 'Ты написал:' в начало твоего сообщения!
            В любой момент ты можешь написать команду '/help' (без кавычек) и \
            тогда я тебе напомню как со мной работать! Приятного использования!""";

    public EchoBot(String botToken) {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            if (Objects.equals(message_text, "/help") ||  Objects.equals(message_text, "/start")){
                message_text = START_MESSAGE;
            }
            else {
                message_text = "Ты написал: " + message_text;
            }

            SendMessage message = SendMessage
                    .builder()
                    .chatId(chat_id)
                    .text(message_text)
                    .build();

            try {
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}