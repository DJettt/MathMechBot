package ru.urfu;

import java.lang.reflect.Constructor;

/**
 * Основной класс для запуска приложения.
 */
final public class Main {
    /**
     * Приватный конструктор, добавленный для того, чтобы явно сообщить, что не нужно создавать объекты этого класса.
     */
    private Main() {}

    /**
     * Запускает Telegram бота с переданным логическим ядром.
     * @param logicCore логическое ядро (обрабатывает поступающие сообщения)
     * @param env строка, содержащая название переменной окружения, в которой находится токен бота
     * @param botClass класс создаваемого бота
     */
    private static void startBot(LogicCore logicCore, String env, Class<? extends Bot> botClass) {
        String botToken = System.getenv(env);
        if (botToken == null) {
            System.out.println("Couldn't retrieve bot token from " + env);
            return;
        }
        try {
            Constructor<? extends Bot> constructor = botClass.getConstructor(String.class, LogicCore.class);
            Bot bot = constructor.newInstance(botToken, logicCore);
            bot.start();
        } catch (Exception e) {
            // Handle the exception
            e.printStackTrace();
        }
    }

    /**
     * Точка входа в программу.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        final LogicCore logicCore = new EchoBotCore();
        startBot(logicCore, "TGMATHMECHBOT_TOKEN", TelegramBot.class);
        startBot(logicCore, "DISCORDBOT_TOKEN", DiscordBot.class);
    }
}
