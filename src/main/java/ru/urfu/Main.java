package ru.urfu;

import java.lang.reflect.Constructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.bots.Bot;
import ru.urfu.bots.DiscordBot;
import ru.urfu.bots.TelegramBot;
import ru.urfu.logics.LogicCore;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.UserArrayStorage;
import ru.urfu.mathmechbot.storages.UserEntryArrayStorage;

/**
 * <p>Основной класс для запуска приложения.</p>
 */
public final class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * <p>Приватный конструктор, добавленный для того, чтобы явно
     * сообщить, что не нужно создавать объекты этого класса.</p>
     */
    private Main() {}

    /**
     * <p>Запускает Telegram бота с переданным логическим ядром.</p>
     *
     * @param logicCore логическое ядро (обрабатывает поступающие сообщения)
     * @param env строка, содержащая название переменной окружения,
     *            в которой находится токен бота
     * @param botClass класс создаваемого бота
     */
    private static void startBot(
            @NotNull LogicCore logicCore,
            String env,
            @NotNull Class<? extends Bot> botClass) {

        String botToken = System.getenv(env);
        if (botToken == null) {
            LOGGER.error("Couldn't retrieve bot token from {}", env);
            return;
        }
        try {
            Constructor<? extends Bot> constructor = botClass
                    .getConstructor(String.class, LogicCore.class);
            Bot bot = constructor.newInstance(botToken, logicCore);
            bot.start();
        } catch (Exception e) {
            LOGGER.error("Error during starting of the bot", e);
        }
    }

    /**
     * <p>Точка входа в программу.</p>
     *
     * <p>Создаём хранилище пользователей и их записей,
     * создаём объект логического ядра, запускаем ботов.</p>
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        final MathMechStorage storage = new MathMechStorage(
                new UserArrayStorage(),
                new UserEntryArrayStorage());

        final LogicCore logicCore = new MMBCore(storage);

        startBot(logicCore, "TGMATHMECHBOT_TOKEN", TelegramBot.class);
        startBot(logicCore, "DISCORDBOT_TOKEN", DiscordBot.class);
    }
}
