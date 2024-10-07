package ru.urfu;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                .setActivity(Activity.playing("IDEA Intellij"))
                .build();

        LOGGER.info("Discord bot successfully started!");
    }

    /*
     * Для бота сообщение в текстовом канале НА СЕРВЕРЕ используется TextChannel
     * а для использования в ЛИЧНОМ СООБЩЕНИИ используется PrivateChannel
     * (я до конца не разобрался почему именно сейчас это работает только так,
     * так как до этого мы использовали только TextChannel и все работало корректно и там и там)
     */
    @Override
    public void sendMessage(LocalMessage message, Long id) {
        final TextChannel textChannel = jda.getTextChannelById(id);
        final PrivateChannel privateChannel = jda.getPrivateChannelById(id);

        if (textChannel != null) {
            MessageCreateAction messageCreateAction = textChannel.sendMessage(message.getText());
            if (message.hasButtons()) {
                messageCreateAction = messageCreateAction.setActionRow(createButtons(message.getButtons()));
            }
            messageCreateAction.queue();
        }
        else if (privateChannel != null) {
            MessageCreateAction messageCreateAction = privateChannel.sendMessage(message.getText());
            if (message.hasButtons()) {
                messageCreateAction = messageCreateAction.setActionRow(createButtons(message.getButtons()));
            }
            messageCreateAction.queue();
        }
        else {
            LOGGER.warn("Unknown message source!");
        }
    }

    /**
     * Создаёт объекты класса Message из дискордоских MessageReceivedEvent.
     * @param event ивент сообщения
     * @return то же сообщение в формате Message для общения с ядром
     */
    private LocalMessage createFromDiscordMessage(MessageReceivedEvent event){
        return new LocalMessage(event.getMessage().getContentDisplay());
    }

    /**
     * Создает кнопку в нужном формате для бота Discord.
     * @param btn кнопка, которую отправило ядро
     * @return возвращает кнопку формата Discord
     */
    private Button createButton(LocalButton btn){
        return Button.primary(btn.data(), btn.name());
    }

    /**
     * Создает кнопки в сообщении.
     * @param buttonGrid локальные кнопки, которые ядро отправило боту.
     * @return возвращает готовые кнопки в нужном формате для бота в Discord.
     */
    private List<Button> createButtons(List<List<LocalButton>> buttonGrid){
        final List<LocalButton> localButtons = new ArrayList<>();
        for (List<LocalButton> buttonRow : buttonGrid){
            localButtons.addAll(buttonRow);
        }

        final List<Button> buttons = new ArrayList<>();
        for (LocalButton localButton : localButtons){
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
