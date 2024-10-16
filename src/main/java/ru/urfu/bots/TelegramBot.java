package ru.urfu.bots;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
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
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.LogicCore;


/**
 * Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore).
 */
public final class TelegramBot implements Bot, LongPollingSingleThreadUpdateConsumer {
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
    public TelegramBot(@NotNull String token, @NotNull LogicCore core) {
        telegramClient = new OkHttpTelegramClient(token);
        logicCore = core;
        botToken = token;
        botsApplication = new TelegramBotsLongPollingApplication();
    }

    /**
     * Запуск бота в отдельном потоке.
     */
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

    /**
     * Отправление сообщения, формат которого содержится в msg.
     * @param msg вся информация о том, что должно содержаться в сообщении
     * @param id id пользователя
     */
    @Override
    public void sendMessage(@NotNull LocalMessage msg, @NotNull Long id) {
        try {
            if (msg.text() != null) {
                telegramClient.execute(createSendMessage(msg, id));
            }
        } catch (TelegramApiException e) {
            LOGGER.error("Couldn't send message", e);
        }
    }

    /**
     *  Разделяет список кнопок на строки кнопок определенного размера для более аккуратного вывода кнопок.
     * @param buttons список кнопок
     * @param sizeOfSquare количество кнопок, которое должно быть в строчке
     * @return возвращает сетку кнопок нужного для вывода формата.
     */
    private List<List<LocalButton>> splitButtonList(List<LocalButton> buttons, int sizeOfSquare) {
        List<List<LocalButton>> splitedButtonList = new ArrayList<>();
        int count = 0;
        while (count < buttons.size()) {
            List<LocalButton> buttonsRow = new ArrayList<>();
            for (int i = 0; i < buttons.size() && i < sizeOfSquare && count < buttons.size(); i++) {
                buttonsRow.add(buttons.get(count));
                count++;
            }
            splitedButtonList.add(buttonsRow);
        }
        return splitedButtonList;
    }

    /**
     *  Подсчитывает сколько кнопок должно быть в ряду в сообщении.
     * @param listSize все кнопки, которые нужно добавить
     * @return возвращает количество кнопок должно быть в ряду в сообщении.
     */
    private int calculateSizeOfSquare(int listSize) {
        return (int) Math.sqrt(listSize) + 1;
    }


    /**
     * Создание кнопок после сообщения.
     * @param localButton информация об одной кнопке, которую нужно создать в сообщении
     * @return возвращает кнопку формата Telegram бота
     */
    private InlineKeyboardButton createButton(LocalButton localButton) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton(localButton.name());
        inlineKeyboardButton.setCallbackData(localButton.data());
        return inlineKeyboardButton;
    }

    /**
     * Создание ряда кнопок.
     * @param localButtonList контейнер кнопок, который нужно создать
     * @return возвращает готовый контейнер кнопок
     */
    private InlineKeyboardRow createButtonsRow(List<LocalButton> localButtonList) {
        InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow();
        for (LocalButton btn : localButtonList) {
            inlineKeyboardRow.add(createButton(btn));
        }
        return inlineKeyboardRow;
    }

    /**
     * Создание сетки кнопок.
     * @param localButtons информация о кнопках, которые нужно вставить в сообщение
     * @return возвращает сетку кнопок
     */
    private InlineKeyboardMarkup createButtons(List<LocalButton> localButtons) {
        int size = calculateSizeOfSquare(localButtons.size());
        List<List<LocalButton>> splitedButtonList = splitButtonList(localButtons, size);
        List<InlineKeyboardRow> keyboard = new ArrayList<>();
        for (List<LocalButton> localButtonRow : splitedButtonList) {
            keyboard.add(createButtonsRow(localButtonRow));
        }
        return new InlineKeyboardMarkup(keyboard);
    }

    /**
     * Превращает LocalMessage в SendMessage.
     * Стоит использовать в тех случаях, когда нет картинок.
     * @param msg  объект сообщения
     * @param chatId id чата, куда надо отправить сообщение
     * @return объект SendMessage, который можно отправлять
     */
    private SendMessage createSendMessage(LocalMessage msg, long chatId) {
        SendMessage.SendMessageBuilder<?, ?> sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(msg.text());
        if (msg.hasButtons()) {
            assert msg.buttons() != null;
            sendMessage = sendMessage.replyMarkup(createButtons(msg.buttons()));
        }
        return sendMessage.build();
    }

    /**
     * Переводит Telegram-сообщения в наши сообщения.
     * @param message объект сообщения из TelegramBots
     * @return объект нашего универсального сообщения
     */
    private LocalMessage convertTelegramMessage(org.telegram.telegrambots.meta.api.objects.message.Message message) {
        final String text = (message.hasPhoto()) ? message.getCaption() : message.getText();
        return new LocalMessage(text, null);
    }

    @Override
    public void consume(Update update) {
        LocalMessage msg;
        long chatId;

        if (update.hasCallbackQuery()) {
            msg = new LocalMessage(update.getCallbackQuery().getData(), null);
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else if (update.hasMessage()) {
            msg = convertTelegramMessage(update.getMessage());
            chatId = update.getMessage().getChatId();
        } else {
            LOGGER.error("Unknown message type!");
            return;
        }
        logicCore.processMessage(new Request(chatId, msg, this));
    }
}
