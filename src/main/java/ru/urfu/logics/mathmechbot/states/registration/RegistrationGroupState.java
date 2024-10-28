package ru.urfu.logics.mathmechbot.states.registration;


import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserEntryStorage;
import ru.urfu.logics.mathmechbot.storages.UserStorage;


/**
 * Состояние запроса номера группы во время регистрации.
 */
public final class RegistrationGroupState implements MathMechBotState {
    private final Pattern validGroupStringPattern = Pattern.compile("^[1-5]$");

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("Какая у Вас группа?")
            .buttons(List.of(
                    new LocalButton("1 группа", "1"),
                    new LocalButton("2 группа", "2"),
                    new LocalButton("3 группа", "3"),
                    new LocalButton("4 группа", "4"),
                    new LocalButton("5 группа", "5"),
                    backButton))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        switch (message.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, chatId, message, bot);
            case null -> bot.sendMessage(tryAgain, chatId);
            default -> textCommandHandler(contextCore, chatId, message, bot);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        return onEnterMessage;
    }

    /**
     * Возвращаем пользователя на шаг назад, то есть на запрос года обучения.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.REGISTRATION_SPECIALTY);
        final LocalMessage msg = new RegistrationSpecialtyState().enterMessage(contextCore, chatId, message, bot);
        bot.sendMessage(msg, chatId);
    }

    /**
     * Проверяем различные текстовые сообщения.
     *
     * @param contextCore логического ядро (контекст для состояния).
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void textCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        assert message.text() != null;

        final UserStorage userStorage = contextCore.getStorage().getUsers();
        final UserEntryStorage userEntryStorage = contextCore.getStorage().getUserEntries();

        if (!validGroupStringPattern.matcher(message.text()).matches()) {
            bot.sendMessage(tryAgain, chatId);
            bot.sendMessage(onEnterMessage, chatId);
            return;
        }

        userEntryStorage.changeUserEntryGroup(chatId,
                Integer.parseInt(message.text()));
        userStorage.changeUserState(chatId, MathMechBotUserState.REGISTRATION_MEN);

        final LocalMessage msg = new RegistrationMenGroupState().enterMessage(contextCore, chatId, message, bot);
        bot.sendMessage(msg, chatId);
    }
}
