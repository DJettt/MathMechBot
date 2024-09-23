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
    private static void startTgBot(){
        String telegramBotToken = System.getenv("TGMATHMECHBOT_TOKEN");
        if (telegramBotToken == null) {
            System.out.println("Couldn't retrieve bot token from TGMATHMECHBOT_TOKEN");
            return;
        }

        new Thread(() -> {
            try (TelegramBotsLongPollingApplication botsApplication = new TelegramBotsLongPollingApplication()) {
                botsApplication.registerBot(telegramBotToken, new EchoBot(telegramBotToken));
                System.out.println("EchoBot successfully started!");

                Thread.currentThread().join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    private static void startDsBot(){
        EchoBotDiscord bot = new EchoBotDiscord();

        String discordBotToken = System.getenv("DISCORDBOT_TOKEN");
        if (discordBotToken == null) {
            System.out.println("Couldn't retrieve bot token from DISCORDBOT_TOKEN");
            return;
        }

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
        startTgBot();
        startDsBot();
    }
}