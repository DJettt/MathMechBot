package ru.urfu.bots;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
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

    /**
     * Запускает бота.
     */
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
                .setActivity(Activity.playing("IDEA Intellij"))
                .build();

        LOGGER.info("Discord bot successfully started!");
    }
    /**
     * Разделяет один большой список кнопок на несколько других размером не больше 5.
     * @param message принимает LocalMessage, откуда берет список кнопок.
     * @return возвращает ArrayList списков, чтобы бот отправлял их по очерди в каждом сообщении.
     */
    private ArrayList<List<LocalButton>> splitButtons(LocalMessage message){
        ArrayList<List<LocalButton>> arrayOfButtons = new ArrayList<>();
        if (message.getButtons().size() <= 5) {
            arrayOfButtons.add(message.getButtons());
        } else {
            int buttonIndex = 0;
            while (buttonIndex < message.getButtons().size()) {
                List<LocalButton> localListOfFiveButtons = new ArrayList<>();
                for (int i = 0; i < 5  && buttonIndex < message.getButtons().size(); i++, buttonIndex++) {
                    localListOfFiveButtons.add(message.getButtons().get(buttonIndex));
                }
                arrayOfButtons.add(localListOfFiveButtons);
            }
        }
        return arrayOfButtons;
    }

    /**
     * Для бота сообщение в текстовом канале НА СЕРВЕРЕ используется TextChannel
     * а для использования в ЛИЧНОМ СООБЩЕНИИ используется PrivateChannel
     * (я до конца не разобрался почему именно сейчас это работает только так,
     * так как до этого мы использовали только TextChannel и все работало корректно и там и там).
     * @param message LocalMessage со всей информацией о сообщении, которое нужно отправить.
     * @param id id чата куда нужно отправить сообщение.
     */
    @Override
    public void sendMessage(LocalMessage message, Long id) {
        MessageChannel channel = jda.getTextChannelById(id);
        if (channel == null) {
            channel = jda.getPrivateChannelById(id);
        }
        if (channel == null) {
            LOGGER.warn("Couldn't find channel to send message to. Given ID: {}", id);
            return;
        }
        if (message.getText() != null) {
            MessageCreateAction messageCreateAction = channel.sendMessage(message.getText());
            if (message.hasButtons()) {
                ArrayList<List<LocalButton>> splitButtons = splitButtons(message);
                boolean first = true;
                for (List<LocalButton> buttons : splitButtons) {
                    if (first) {
                        messageCreateAction = messageCreateAction.setActionRow(createButtons(buttons));
                        messageCreateAction.queue();
                        first = false;
                    } else {
                        MessageCreateAction messageCreateActionSub = channel.sendMessage("")
                                .setActionRow(createButtons(buttons));
                        messageCreateActionSub.queue();
                    }
                }
            } else {
                messageCreateAction.queue();
            }
        } else {
            LOGGER.warn("Unknown message source!");
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

    /**
     * Создает кнопку в нужном формате для бота Discord.
     * @param btn кнопка, которую отправило ядро
     * @return возвращает кнопку формата Discord
     */
    private Button createButton(LocalButton btn) {
        return Button.primary(btn.data(), btn.name());
    }

    /**
     * Создает кнопки в сообщении.
     * @param buttonRow локальные кнопки, которые ядро отправило боту.
     * @return возвращает готовые кнопки в нужном формате для бота в Discord.
     */
    private List<Button> createButtons(List<LocalButton> buttonRow) {
        final List<Button> buttons = new ArrayList<>();
        for (LocalButton localButton : buttonRow) {
            buttons.add(createButton(localButton));
        }
        return buttons;
    }

    /**
     * Отслеживает отправление сообщения от пользователя боту.
     * @param event содержит всю информацию об обновлениях в чате.
     */
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        LocalMessage msg = createFromDiscordMessage(event);
        final LocalMessage response = logicCore.processMessage(msg);
        sendMessage(response, event.getChannel().getIdLong());
    }

    /**
     * Отслеживает взаимодействия с кнопками.
     * @param event содержит всю информацию об обновлениях.
     */
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        LocalMessage msg = new LocalMessage(event.getButton().getId());
        LocalMessage response = logicCore.processMessage(msg);
        sendMessage(response, event.getChannelIdLong());
    }
}
