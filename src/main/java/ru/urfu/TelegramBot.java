package ru.urfu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
    public void sendMessage(LocalMessage msg, Long id){
        try {
            telegramClient.execute(createFromMessage(msg, id));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создание кнопок после сообщения.
     * @param name текст, который будет написан на кнопке.
     * @param data текстовая информация, которую бот получит
     * при нажатии пользователя на кнопку.
     */
    private InlineKeyboardButton createButton(String name, String data){
        return InlineKeyboardButton
                .builder()
                .text(name)
                .callbackData(data)
                .build();
    }

    /**
     * Превращает наш Message в телеграмный SendMessage
     * @param msg объект нашего универсального сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendMessage, который можно отправлять
     */
    private SendMessage createFromMessage(LocalMessage msg, long chatId) {
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
    private LocalMessage createFromTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        String message_text = message.getText();
        return new LocalMessage(message_text);
    }

    /**
     * Принимает какое-либо обновление в чате с ботом.
     * @param update объект, в котором содержится информация об обновлении в чате.
     */
    //TODO: разобраться с этим методом!
    @Override
    public void consume(Update update) {
        String data;
        LocalMessage msg;
        if (update.hasCallbackQuery()){
            data = update.getCallbackQuery().getData();
            msg = new LocalMessage(data);
        }
        else if(update.hasMessage()) {
            data = update.getMessage().getText();
            msg = createFromTelegramMessage(update.getMessage());
        }
        //TODO: убрать костыль!!!!
        else {
            msg = new LocalMessage("");
        }
        final long chatId = update.getMessage().getChatId();
        final LocalMessage response = logicCore.processMessage(msg);
        if (response == null) {
            return;
        }
        sendMessage(response, chatId);
    }
}