package ru.urfu;

import java.lang.reflect.Constructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.bots.TelegramBot;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;
import ru.urfu.logics.mathmechbot.storages.UserArrayStorage;
import ru.urfu.logics.mathmechbot.storages.UserEntryArrayStorage;

/**
 * Основной класс для запуска приложения.
 */
final public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

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
            LOGGER.error("Couldn't retrieve bot token from {}", env);
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

    /**
     * Точка входа в программу.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        final MathMechStorage storage = new MathMechStorage(new UserArrayStorage(), new UserEntryArrayStorage());
        final LogicCore logicCore = new MathMechBotCore(storage);
        startBot(logicCore, "TGMATHMECHBOT_TOKEN", TelegramBot.class);

//        Выключил, потому что его нормально не запустить из-за блокировки.
//        startBot(logicCore, "DISCORDBOT_TOKEN", DiscordBot.class);
    }
}
