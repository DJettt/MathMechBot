package ru.urfu.bots;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.Request;
import ru.urfu.logics.LogicCore;

/**
 * Простой дискорд-бот, который принимает текстовые сообщения и составляет ответ
 * в зависимости от переданного ему при создании логического ядра (logicCore).
 */
public final class DiscordBot extends ListenerAdapter implements Bot {
    private final Logger logger = LoggerFactory.getLogger(DiscordBot.class);
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
                .build();

        logger.info("Discord bot successfully started!");
    }

    /**
     * Разделяет один большой список кнопок на несколько других размером не больше 5.
     * @param message принимает LocalMessage, откуда берет список кнопок.
     * @return возвращает список списков, чтобы бот отправлял их по очереди в каждом сообщении.
     */
    private List<List<LocalButton>> splitButtons(LocalMessage message) {
        assert message.buttons() != null;

        final int maxSize = 5;
        List<List<LocalButton>> arrayOfButtons = new ArrayList<>();

        if (message.buttons().size() <= maxSize) {
            arrayOfButtons.add(message.buttons());
        } else {
            int buttonIndex = 0;
            while (buttonIndex < message.buttons().size()) {
                List<LocalButton> localListOfFiveButtons = new ArrayList<>();
                for (int i = 0; i < maxSize  && buttonIndex < message.buttons().size(); i++, buttonIndex++) {
                    localListOfFiveButtons.add(message.buttons().get(buttonIndex));
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
    public void sendMessage(@NotNull LocalMessage message, @NotNull Long id) {
        MessageChannel channel = jda.getTextChannelById(id);
        if (channel == null) {
            channel = jda.getPrivateChannelById(id);
        }
        if (channel == null) {
            logger.warn("Couldn't find channel to send message to. Given ID: {}", id);
            return;
        }
        if (message.text() != null) {
            MessageCreateAction messageCreateAction = channel.sendMessage(message.text());
            if (message.hasButtons()) {
                List<List<LocalButton>> splitButtons = splitButtons(message);
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
            logger.warn("Unknown message source!");
        }
    }

    /**
     * Создаёт объекты класса LocalMessage из дискордовских Message.
     * @param message полученное сообщение
     * @return то же сообщение в формате LocalMessage для общения с ядром
     */
    private LocalMessage convertDiscordMessage(net.dv8tion.jda.api.entities.Message message) {
        return new LocalMessage(message.getContentDisplay(), null);
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
        LocalMessage msg = convertDiscordMessage(event.getMessage());
        logicCore.processMessage(new Request(event.getChannel().getIdLong(), msg, this));
    }

    /**
     * Отслеживает взаимодействия с кнопками.
     * @param event содержит всю информацию об обновлениях.
     */
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        LocalMessage msg = new LocalMessage(event.getButton().getId(), null);
        logicCore.processMessage(new Request(event.getChannel().getIdLong(), msg, this));
    }
}
