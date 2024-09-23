package ru.urfu;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Основной класс для запуска приложения
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String botToken = System.getenv("MATHMECHBOT_TOKEN");
        if (botToken == null) {
            LOGGER.error("Couldn't retrieve bot token from MATHMECHBOT_TOKEN.");
            return;
        }

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new EchoBot(botToken));
            LOGGER.info("EchoBot successfully started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            LOGGER.error("Exception during bot registration.", e);
        }
    }
}