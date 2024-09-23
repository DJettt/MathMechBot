package ru.urfu;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


/**
 * Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore)
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final LogicCore logicCore;

    /** Переводит Telegram-сообщения в наши сообщения
     * @param message объект сообщения из TelegramBots
     * @return объект нашего универсального сообщения
     */
    private Message createFromTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        String message_text = message.getText();
        return new Message(message_text);
    }

    /** Превращает наш Message в телеграмный SendMessage
     * @param msg объект нашего универсального сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendMessage, который можно отправлять
     */
    private SendMessage createFromMessage(Message msg, long chatId) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(msg.getText())
                .build();
    }

    @Override
    public void consume(Update update) {
        final long chatId = update.getMessage().getChatId();

        final Message response = logicCore.processMessage(createFromTelegramMessage(update.getMessage()));
        if (response == null) {
            return;
        }

        try {
            telegramClient.execute(createFromMessage(response, chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public TelegramBot(String botToken, LogicCore core) {
        telegramClient = new OkHttpTelegramClient(botToken);
        logicCore = core;
    }
}