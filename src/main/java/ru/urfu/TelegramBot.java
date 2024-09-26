package ru.urfu;

import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


/**
 * Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore)
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer, Bot {
    private final TelegramClient telegramClient;
    private final LogicCore logicCore;
    private final String botToken;
    private final TelegramBotsLongPollingApplication botsApplication;

    public TelegramBot(String token, LogicCore core) {
        telegramClient = new OkHttpTelegramClient(token);
        logicCore = core;
        botToken = token;
        botsApplication = new TelegramBotsLongPollingApplication();
    }

    @Override
    public void start() {
        new Thread(() -> {
            try {
                botsApplication.registerBot(botToken, this);
                System.out.println("Telegram bot successfully started!");

                Thread.currentThread().join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void sendMessage(Message msg, Long id) {
        try {
            telegramClient.execute(createFromMessage(msg, id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Превращает наш Message в телеграмный SendMessage
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

    /**
     * Переводит Telegram-сообщения в наши сообщения
     * @param message объект сообщения из TelegramBots
     * @return объект нашего универсального сообщения
     */
    private Message createFromTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        String messageText = message.getText();
        return new Message(messageText);
    }

    @Override
    public void consume(Update update) {
        final long chatId = update.getMessage().getChatId();
        final Message msg = createFromTelegramMessage(update.getMessage());

        final Message response = logicCore.processMessage(msg);
        if (response == null) {
            return;
        }
        sendMessage(response, chatId);
    }
}
