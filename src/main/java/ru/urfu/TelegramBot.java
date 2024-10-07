package ru.urfu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;


/**
 * Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore).
 */
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer, Bot {
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

    /**
     * Запуск бота в отдельном потоке.
     */
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

    /**
     * Отправление сообщения, формат которого содержится в msg
     * @param msg вся информация о том, что должно содержаться в сообщении
     * @param id id пользователя
     */
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
     * @param localButton информация об одной кнопке, которую нужно создать в сообщении
     */
    private InlineKeyboardButton createButton(LocalButton localButton) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(localButton.name());
        inlineKeyboardButton.setCallbackData(localButton.data());
        return inlineKeyboardButton;
    }

    /**
     * Создание кнопок.
     * @param localButtons информация о кнопках, которые нужно вставить в сообщение
     */
    private InlineKeyboardMarkup createButtons(List<List<LocalButton>> localButtons) {
        List<InlineKeyboardRow> keyboard = new ArrayList<>();

        for (List<LocalButton> buttonRow : localButtons) {
            if (!buttonRow.isEmpty()) { // проверка, что внутренний список не пустой
                List<InlineKeyboardButton> inlineButtons = new ArrayList<>();
                for (LocalButton localButton : buttonRow) {
                    inlineButtons.add(createButton(localButton));
                }
                keyboard.add(new InlineKeyboardRow(inlineButtons));
            }
        }

        if (keyboard.isEmpty()) {
            return null;
        } else {
            return new InlineKeyboardMarkup(keyboard);
        }
    }

    /**
     * Превращает наш LocalMessage в телеграмный SendMessage и
     * по статусу LocalMessage определяет какой тип сообщения нужно отправить
     * @param msg объект нашего универсального сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendMessage, который можно отправлять
     */
    private SendMessage createFromMessage(LocalMessage msg, long chatId) {
        SendMessage.SendMessageBuilder<?, ?> sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(msg.getText());
        if (msg.hasButtons()) {
            sendMessage = sendMessage.replyMarkup(createButtons(msg.getButtons()));
        }
        return sendMessage.build();
    }

//    /**
//     * Переводит Telegram-сообщения в наши сообщения.
//     * @param message объект сообщения из TelegramBots
//     * @return объект нашего универсального сообщения
//     */
//    private LocalMessage createFromTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
//        String messageText = message.getText();
//        return new LocalMessage(messageText);
//    }

    /**
     * Принимает какое-либо обновление в чате с ботом.
     * @param update объект, в котором содержится информация об обновлении в чате.
     */
    @Override
    public void consume(Update update) {
        LocalMessage msg;
        long chatId;

        if (update.hasCallbackQuery()) {
            msg = new LocalMessage(update.getCallbackQuery().getData());
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else if (update.hasMessage()) {
            msg = new LocalMessage(update.getMessage().getText());
            chatId = update.getMessage().getChatId();
        } else {
            LOGGER.error("Unknown message type!");
            return;
        }

        final LocalMessage response = logicCore.processMessage(msg);
        if (response == null) {
            return;
        }
        sendMessage(response, chatId);
    }
}