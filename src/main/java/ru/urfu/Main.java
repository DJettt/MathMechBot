package ru.urfu;

import ru.urfu.bots.TelegramBot;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;

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
        final MathMechStorage storage = new MathMechStorage();
        final LogicCore logicCore = new MathMechBotCore(storage);

        final TelegramBot telegramBot = new TelegramBot(
                System.getenv("TGMATHMECHBOT_TOKEN"), logicCore);
        telegramBot.start();

//        final DiscordBot discordBot = new DiscordBot(
//                System.getenv("DISCORDBOT_TOKEN"), logicCore);
//        discordBot.start();
    }
}
