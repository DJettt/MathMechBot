package ru.urfu;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBot extends ListenerAdapter {
    private final LogicCore logicCore;

    public DiscordBot(String token,LogicCore core){
        logicCore = core;
    }

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
