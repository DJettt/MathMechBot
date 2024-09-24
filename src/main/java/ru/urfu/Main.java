package ru.urfu;

import java.lang.reflect.Constructor;
/**
 * Основной класс для запуска приложения
 */
public class Main {
    /**
     * Запускает Telegram бота с переданным логическим ядром
     * @param logicCore логическое ядро (обрабатывает постпающие сообщения)
     */
    private static void startBot(LogicCore logicCore, String env, Class <? extends Bot> botClass){
        String telegramBotToken = System.getenv(env);
        if (telegramBotToken == null) {
            System.out.println("Couldn't retrieve bot token from " + env);
            return;
        }
        try {
            Constructor<? extends Bot> constructor = botClass.getConstructor(String.class, LogicCore.class);
            Bot myBot = constructor.newInstance(telegramBotToken, logicCore);
            myBot.start();
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        final LogicCore logicCore = new EchoBotCore();
        startBot(logicCore, "TGMATHMECHBOT_TOKEN", TelegramBot.class);
        startBot(logicCore, "DISCORDBOT_TOKEN", DiscordBot.class);
    }
}