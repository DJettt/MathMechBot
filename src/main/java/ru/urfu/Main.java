package ru.urfu;

import java.lang.reflect.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Основной класс для запуска приложения
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * Запускает Telegram бота с переданным логическим ядром
     * @param logicCore логическое ядро (обрабатывает постпающие сообщения)
     */
    private static void startBot(LogicCore logicCore, String env, Class <? extends Bot> botClass){
        String botToken = System.getenv(env);
        if (botToken == null) {
            LOGGER.error("Couldn't retrieve bot token from " + env);
            return;
        }
        try {
            Constructor<? extends Bot> constructor = botClass.getConstructor(String.class, LogicCore.class);
            Bot bot = constructor.newInstance(botToken, logicCore);
            bot.start();
        } catch (Exception e) {
            LOGGER.error("Error during starting of the bot", e);
        }
    }
    public static void main(String[] args) {
        final LogicCore logicCore = new EchoBotCore();
        startBot(logicCore, "TGMATHMECHBOT_TOKEN", TelegramBot.class);
        startBot(logicCore, "DISCORDBOT_TOKEN", DiscordBot.class);
    }
}