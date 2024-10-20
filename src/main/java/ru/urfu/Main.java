package ru.urfu;

import ru.urfu.bots.DiscordBot;
import ru.urfu.bots.TelegramBot;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.storages.MathMechStorage;

/**
 * Основной класс для запуска приложения.
 */
final public class Main {
    /**
     * Приватный конструктор, добавленный для того, чтобы явно сообщить, что не нужно создавать объекты этого класса.
     */
    private Main() {
    }

    /**
     * Точка входа в программу.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        final MathMechStorage storage = new MathMechStorage();
        final LogicCore logicCore = new MathMechBotCore(storage);

        final TelegramBot telegramBot = new TelegramBot(
                System.getenv("TGMATHMECHBOT_TOKEN"), logicCore);
        telegramBot.start();

        final DiscordBot discordBot = new DiscordBot(
                System.getenv("DISCORDBOT_TOKEN"), logicCore);
        discordBot.start();
    }
}
