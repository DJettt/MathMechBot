package ru.urfu;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

import java.util.List;

/**
 * Основной класс для запуска приложения
 */
public class Main {
    /** Запускает Telegram бота с переданным логическим ядром
     * @param logicCore логическое ядро (обрабатывает постпающие сообщения)
     */
    private static void startTelegramBot(LogicCore logicCore) {
        String telegramBotToken = System.getenv("TGMATHMECHBOT_TOKEN");
        if (telegramBotToken == null) {
            System.out.println("Couldn't retrieve bot token from TGMATHMECHBOT_TOKEN");
            return;
        }

        new Thread(() -> {
            try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
                botsApplication.registerBot(telegramBotToken, new TelegramBot(telegramBotToken, logicCore));
                System.out.println("EchoBot successfully started!");

                Thread.currentThread().join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    private static void startDiscordBot(LogicCore logicCore){
        String discordBotToken = System.getenv("DISCORDBOT_TOKEN");
        if (discordBotToken == null) {
            System.out.println("Couldn't retrieve bot token from DISCORDBOT_TOKEN");
            return;
        }
        DiscordBot bot = new DiscordBot(discordBotToken, logicCore);

        //TODO: проверить на возникновение исключений
        JDABuilder.createLight(discordBotToken)
                .addEventListeners(bot)
                .enableIntents(
                        List.of(
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT
                        )
                )
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("Klepinin's lections"))
                .build();
        System.out.println("ds bot got created!");
    }
    public static void main(String[] args) {
        final LogicCore logicCore = new EchoBotCore();
        startTelegramBot(logicCore);
        startDiscordBot(logicCore);
    }
}