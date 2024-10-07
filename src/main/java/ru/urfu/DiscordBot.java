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
            switch (message.getStatus()) {
                case "text_only" ->
                    textChannel.sendMessage(message.getText()).queue();
                case "text_with_buttons" -> textChannel.sendMessage(message.getText())
                        .setActionRow(createButtons(message.getButtons())).queue();
                default -> {
                    LOGGER.error("DiscordBot:: Incorrect LocalMessage status!");
                    textChannel.sendMessage("Извините, произошла непредвиденная ошибка. Скоро все починим!").queue();
                }
            }
        } else if (privateChannel != null) {
            switch (message.getStatus()) {
                case "text_only" ->
                        privateChannel.sendMessage(message.getText()).queue();
                case "text_with_buttons" -> privateChannel.sendMessage(message.getText())
                        .setActionRow(createButtons(message.getButtons())).queue();
                default -> {
                    LOGGER.error("DiscordBot:: Incorrect LocalMessage status!");
                    privateChannel.sendMessage("Извините, произошла непредвиденная ошибка. Скоро все починим!").queue();
                }
            }
        } else {
            LOGGER.error("DiscordBot:: Unknown message source!");
        }
    }

    /**
     * Создаёт объекты класса Message из дискордоских MessageReceivedEvent.
     * @param event ивент сообщения
     * @return то же сообщение в формате Message для общения с ядром
     */
    private LocalMessage createFromDiscordMessage(MessageReceivedEvent event) {
        return new LocalMessage(event.getMessage().getContentDisplay(), sendBackStatusMessage());
    }

    /**
     * Создает кнопку в нужном формате для бота Discord.
     * @param btn кнопка, которую отправило ядро
     * @return возвращает кнопку формата Discord
     */
    private Button createButton(LocalButton btn) {
        return Button.primary(btn.getData(), btn.getName());
    }

    /**
     * Создает кнопки в сообщении.
     * @param btn локальные кнопки, которые ядро отправило боту.
     * @return возвращает готовые кнопки в нужном формате для бота в Discord.
     */
    private ArrayList<Button> createButtons(List<ArrayList<LocalButton>> btn) {
        ArrayList<LocalButton> localButtons = new ArrayList<>();
        for (ArrayList<LocalButton> buttonsRow : btn) {
            localButtons.addAll(buttonsRow);
        }
        ArrayList<Button> buttons = new ArrayList<>();
        for (LocalButton localBtn : localButtons) {
            buttons.add(createButton(localBtn));
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
    public void onButtonInteraction(ButtonInteractionEvent event) {
        LocalMessage msg;
        Long id = event.getChannelIdLong();
        if (event.getButton().getId().equals("button_1")) {
            msg = new LocalMessage(event.getButton().getId(), sendBackStatusCallback());
            LocalMessage response = logicCore.processMessage(msg);
            sendMessage(response, id);
        } else if (event.getButton().getId().equals("/help")) {
            msg = new LocalMessage(event.getButton().getId(), sendBackStatusCallback());
            LocalMessage response = logicCore.processMessage(msg);
            sendMessage(response, id);
        } else if (event.getButton().getId().equals("button_3")) {
            msg = new LocalMessage(event.getButton().getId(), sendBackStatusCallback());
            LocalMessage response = logicCore.processMessage(msg);
            sendMessage(response, id);
        } else {
            LOGGER.error("DiscordBot :: Incorrect button id!");
        }
    }

    //TODO: эту и подобные функции было бы лучше сделать по-другому
    /**
     * Возвращает назад статус о том, что информация получена из callBackQuery.
     * @return статус
     */
    private String sendBackStatusCallback() {
        final String STATUS_CALLBACK = "button_interaction";
        return STATUS_CALLBACK;
    }

    /**
     * Возвращает назад статус о том, что информация получена из Message.
     * То есть бот получил обычное сообщение.
     * @return статус
     */
    private String sendBackStatusMessage() {
        final String STATUS_CALLBACK = "message";
        return STATUS_CALLBACK;
    }
}
