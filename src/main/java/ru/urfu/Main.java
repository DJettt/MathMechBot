package ru.urfu;

import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

/**
 * Основной класс для запуска приложения
 */
public class Main {
    /** Запускает Telegram бота с переданным логическим ядром
     * @param logicCore логическое ядро (обрабатывает постпающие сообщения)
     */
    private static void startTelegramBot(LogicCore logicCore) {
        String botToken = System.getenv("MATHMECHBOT_TOKEN");
        if (botToken == null) {
            System.out.println("Couldn't retrieve bot token from MATHMECHBOT_TOKEN");
            return;
        }

        try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
            botsApplication.registerBot(botToken, new TelegramBot(botToken, logicCore));
            System.out.println("Telegram bot successfully started!");

            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        final LogicCore logicCore = new EchoBotCore();
        startTelegramBot(logicCore);
    }
}