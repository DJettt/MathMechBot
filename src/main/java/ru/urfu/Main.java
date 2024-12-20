package ru.urfu;

import ru.urfu.bots.TelegramBot;
import ru.urfu.logics.LogicCore;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.storages.MathMechDbStorage;
import ru.urfu.mathmechbot.timetable.TimetableCachedFactory;
import ru.urfu.mathmechbot.timetable.TimetableFactory;

/**
 * <p>Основной класс для запуска приложения.</p>
 */
final public class Main {
    /**
     * <p>Приватный конструктор, добавленный для того, чтобы явно
     * сообщить, что не нужно создавать объекты этого класса.</p>
     */
    private Main() {
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
        final TimetableFactory timetableFactory = new TimetableCachedFactory();
        final MathMechDbStorage storage = new MathMechDbStorage();
        final LogicCore logicCore = new MMBCore(storage, timetableFactory);

        final TelegramBot telegramBot = new TelegramBot(
                System.getenv("TELEGRAM_BOT_TOKEN"), logicCore);
        telegramBot.start();

//        final DiscordBot discordBot = new DiscordBot(
//                System.getenv("DISCORD_BOT_TOKEN"), logicCore);
//        discordBot.start();
    }
}
