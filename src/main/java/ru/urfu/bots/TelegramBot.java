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
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;


/**
 * <p>Простой телеграм-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore).</p>
 */
public final class TelegramBot implements Bot, LongPollingSingleThreadUpdateConsumer {
    private final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final TelegramClient telegramClient;
    private final LogicCore logicCore;
    private final String botToken;
    private final TelegramBotsLongPollingApplication botsApplication;

    /**
     * <p>Конструктор.</p>
     *
     * @param token строка, содержащая токен для бота
     * @param core  логическое ядро, обрабатывающее сообщения
     */
    public TelegramBot(@NotNull String token, @NotNull LogicCore core) {
        telegramClient = new OkHttpTelegramClient(token);
        logicCore = core;
        botToken = token;
        botsApplication = new TelegramBotsLongPollingApplication();
    }

    /**
     * <p>Запуск бота в отдельном потоке (по умолчанию запускается в текущем).</p>
     */
    public void start() {
        new Thread(() -> {
            try {
                botsApplication.registerBot(botToken, this).start();
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
            logger.info("Telegram bot successfully started!");
        }).start();
    }

    /**
     * <p>Отправление сообщения, формат которого содержится в msg.</p>
     *
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
            logger.error("Couldn't send message", e);
        }
    }

    /**
     * <p>Разделяет список кнопок на строки кнопок определенного
     * размера для более аккуратного вывода кнопок.</p>
     *
     * @param buttons      список кнопок
     * @param sizeOfSquare количество кнопок, которое должно быть в строчке
     * @return возвращает  сетку кнопок нужного для вывода формата.
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
     * <p>Подсчитывает сколько кнопок должно быть в ряду в сообщении.</p>
     *
     * @param listSize все кнопки, которые нужно добавить
     * @return возвращает количество кнопок должно быть в ряду в сообщении.
     */
    private int calculateSizeOfSquare(int listSize) {
        return (int) Math.sqrt(listSize) + 1;
    }


    /**
     * <p>Создание кнопок после сообщения.</p>
     *
     * @param localButton информация об одной кнопке, которую нужно создать в сообщении
     * @return возвращает кнопку формата Telegram бота
     */
    private InlineKeyboardButton createButton(LocalButton localButton) {
        final InlineKeyboardButton inlineKeyboardButton =
                new InlineKeyboardButton(localButton.name());
        inlineKeyboardButton.setCallbackData(localButton.data());
        return inlineKeyboardButton;
    }

    /**
     * <p>Создание ряда кнопок.</p>
     *
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
     * <p>Создание сетки кнопок.</p>
     *
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
     * <p>Превращает LocalMessage в SendMessage.</p>
     *
     * <p>Стоит использовать в тех случаях, когда нет картинок.</p>
     *
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
     * <p>Переводит Telegram-сообщения в LocalMessage.</p>
     *
     * @param message объект сообщения из TelegramBots
     * @return объект нашего универсального сообщения
     */
    private LocalMessage convertTelegramMessage(Message message) {
        final String text = (message.hasPhoto())
                ? message.getCaption()
                : message.getText();
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
            logger.error("Unknown message type! Received update: {}", update);
            return;
        }
        logicCore.processMessage(chatId, msg, this);
    }
}
