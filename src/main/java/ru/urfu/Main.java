package ru.urfu;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 * Основной класс для запуска приложения
 */
public class Main {
    public static void main(String[] args) {
        String botToken = System.getenv("MATHMECHBOT_TOKEN");
        if (botToken == null) {
            System.out.println("Couldn't retrieve bot token from MATHMECHBOT_TOKEN");
            return;
        }

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new EchoBot(botToken));
            System.out.println("EchoBot successfully started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}