package ru.urfu.bots;

import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.Message;
import ru.urfu.logics.LogicCore;

/**
 * Простой дискорд-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore).
 */
public class DiscordBot extends ListenerAdapter implements Bot {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);
    private final LogicCore logicCore;
    private final String botToken;
    private JDA jda;

    /**
     * Конструктор.
     * @param token токен Discord бота
     * @param core логическое ядро, обрабатывающее сообщения
     */
    public DiscordBot(String token, LogicCore core) {
        logicCore = core;
        botToken = token;
    }

    @Override
    public void start() {
        //TODO: проверить на возникновение исключений
        jda = JDABuilder.createLight(botToken)
                .addEventListeners(this)
                .enableIntents(
                        List.of(
                                GatewayIntent.GUILD_MESSAGES,
                                GatewayIntent.MESSAGE_CONTENT
                        )
                )
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("Klepinin's lections"))
                .build();

        LOGGER.info("Discord bot successfully started!");
    }

    @Override
    public void sendMessage(Message message, Long id) {
        MessageChannel channel = jda.getTextChannelById(id);
        if (channel == null) {
            channel = jda.getPrivateChannelById(id);
        }

        if (channel == null) {
            LOGGER.warn("Couldn't find channel to send message to. Given ID: {}", id);
            return;
        }

        if (message.text() != null) {
            channel.sendMessage(message.text()).queue();
        }
    }

    /**
     * Создаёт объекты класса Message из дискордоских MessageReceivedEvent.
     * @param message полученное сообщение
     * @return то же сообщение в формате Message для общения с ядром
     */
    private Message convertDiscordMessage(net.dv8tion.jda.api.entities.Message message) {
        return new Message(message.getContentDisplay());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        final Message msg = convertDiscordMessage(event.getMessage());
        logicCore.processMessage(msg, event.getChannel().getIdLong(), this);
    }
}
