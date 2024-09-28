package ru.urfu;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


/**
 * Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore)
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer, Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramBot.class);
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
    public void start(){
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
    public void sendMessage(Message msg, Long id){
        try {
            if (msg.images().isEmpty()) {
                telegramClient.execute(createSendMessage(msg, id));
            } else if (msg.images().size() == 1) {
                telegramClient.execute(createSendPhoto(msg, id));
            } else {
                telegramClient.execute(createSendMediaGroup(msg, id));
                if (msg.text() != null) {
                    telegramClient.execute(createSendMessage(msg, id));
                }
            }
        } catch (TelegramApiException e) {
            LOGGER.error("Couldn't send message", e);
        }
    }

    /**
     * Превращает Message в SendPhoto.
     * Стоит использовать в тех случаях, когда сообщение содержит одно изображение.
     * @param msg объект сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendPhoto, который можно отправлять
     */
    private SendPhoto createSendPhoto(Message msg, long chatId) {
        if (msg.images().size() > 1) {
            LOGGER.warn("createSendPhoto was called for message with more than one image");
        }
        return SendPhoto
                .builder()
                .chatId(chatId)
                .photo(new InputFile(msg.images().getFirst()))
                .caption(msg.text())
                .build();
    }

    /**
     * Превращает Message в SendMediaGroup.
     * Стоит использовать в тех случаях, когда сообщение содержит несколько медиа-файлов.
     * @param msg объект сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendMediaGroup, который можно отправлять
     */
    private SendMediaGroup createSendMediaGroup(Message msg, long chatId) {
        return SendMediaGroup
                .builder()
                .chatId(chatId)
                .medias(msg.images().stream().map(image -> new InputMediaPhoto(image, "photo")).toList())
                .build();
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
     * Преобразует PhotoSize в java.io.File
     * @param photo изображение в виде объекта PhotoSize
     * @return изображение в виде объекта java.io.File
     */
    private java.io.File downloadFile(PhotoSize photo) {
        try {
            final File telegramFile = telegramClient.execute(GetFile
                    .builder()
                    .fileId(photo.getFileId())
                    .build());
            return telegramClient.downloadFile(telegramFile);
        } catch (TelegramApiException e) {
            LOGGER.error("Error during downloading photos", e);
        }
        return null;
    }

    /**
     * Переводит Telegram-сообщения в наши сообщения
     * @param message объект сообщения из TelegramBots
     * @return объект нашего универсального сообщения
     */
    private Message createFromTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        final String text = (message.hasPhoto()) ? message.getCaption() : message.getText();
        final List<java.io.File> images = new ArrayList<>();

        if (message.hasPhoto()) {
            final PhotoSize photo = message.getPhoto().getLast();
            final java.io.File file = downloadFile(photo);

            if (file != null) {
                images.add(downloadFile(photo));
            }
        }
        return new Message(text, images);
    }

    @Override
    public void consume(Update update) {
        final long chatId = update.getMessage().getChatId();
        final Message msg = createFromTelegramMessage(update.getMessage());
        logicCore.processMessage(msg, chatId, this);
    }
}