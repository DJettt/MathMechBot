package ru.urfu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.urfu.logics.LogicCore;


/**
 * Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore).
 */
public class TelegramBot implements Bot, LongPollingSingleThreadUpdateConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);
    private final TelegramClient telegramClient;
    private final LogicCore logicCore;
    private final String botToken;
    private final TelegramBotsLongPollingApplication botsApplication;

    /**
     * Конструктор.
     * @param token строка, содержащая токен для бота
     * @param core логическое ядро, обрабатывающее сообщения
     */
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
                LOGGER.info("Telegram bot successfully started!");

                Thread.currentThread().join();
            } catch (Exception e) {
                LOGGER.error("Bot didn't get registered and didn't run.", e);
            }
        }).start();
    }

    @Override
    public void sendMessage(Message msg, Long id) {
        try {
            if (msg.text() != null) {
                telegramClient.execute(createSendMessage(msg, id));
            }
        } catch (TelegramApiException e) {
            LOGGER.error("Couldn't send message", e);
        }
    }

    /**
     * Превращает Message в SendMessage.
     * Стоит использовать в тех случаях, когда сообщение содержит лишь текст.
     * @param msg  объект сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendMessage, который можно отправлять
     */
    private SendMessage createSendMessage(Message msg, long chatId) {
        return SendMessage
                .builder()
                .chatId(chatId)
                .text(msg.text())
                .build();
    }

    /**
     * Переводит Telegram-сообщения в наши сообщения.
     * @param message объект сообщения из TelegramBots
     * @return объект нашего универсального сообщения
     */
    private Message convertTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        final String text = (message.hasPhoto()) ? message.getCaption() : message.getText();
        return new Message(text);
    }

    @Override
    public void consume(Update update) {
        final long chatId = update.getMessage().getChatId();
        final Message msg = convertTelegramMessage(update.getMessage());
        logicCore.processMessage(msg, chatId, this);
    }
}