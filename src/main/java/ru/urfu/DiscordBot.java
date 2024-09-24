package ru.urfu;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * Простой дискорд-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore)
 */
public class DiscordBot extends ListenerAdapter {
    private final LogicCore logicCore;

    /**
     * @param token токен Discord бота
     * @param core логическое ядро, обрабатывающее сообщения
     */
    public DiscordBot(String token, LogicCore core){
        logicCore = core;
    }

    /**
     * @param event ивент сообщения
     * @return тоже сообщение в формате Message для общения с ядром
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

        event.getChannel()
                .sendMessage(response.getText())
                .queue();
    }
}
