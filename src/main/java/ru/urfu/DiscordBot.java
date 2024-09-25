package ru.urfu;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.List;

/**
 * Простой дискорд-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore)
 */
public class DiscordBot extends ListenerAdapter implements Bot {
    private final LogicCore logicCore;
    private final String botToken;
    private JDA jda;
    /**
     * @param token токен Discord бота
     * @param core логическое ядро, обрабатывающее сообщения
     */
    public DiscordBot(String token, LogicCore core){
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

        System.out.println("Discord bot successfully started!");
    }

    /**
     *
     * Для бота сообщение в текстовом канале НА СЕРВЕРЕ используется TextChannel
     * а для использования в ЛИЧНОМ СООБЩЕНИИ используется PrivateChannel
     * (я до конца не разобрался почему именно сейчас это работает только так,
     * так как до этого мы использовали только TextChannel и все работало корректно и там и там)
     */
    @Override
    public void sendMessage(Message message, Long id) {
        final TextChannel textChannel = jda.getTextChannelById(id);
        System.out.println(id);
        if (textChannel != null) {
            textChannel.sendMessage(message.getText()).queue();
        }
        else {
            final PrivateChannel privateChannel = jda.getPrivateChannelById(id);
            if (privateChannel != null) {
                privateChannel.sendMessage(message.getText()).queue();
            }
        }
    }

    /**
     * @param event ивент сообщения
     * @return то же сообщение в формате Message для общения с ядром
     */
    private Message createFromDiscordMessage(MessageReceivedEvent event){
        return new Message(event.getMessage().getContentDisplay());
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().isBot()){
            return;
        }
        Message msg = createFromDiscordMessage(event);
        final Message response = logicCore.processMessage(msg);
        sendMessage(response, event.getChannel().getIdLong());

    }
}
