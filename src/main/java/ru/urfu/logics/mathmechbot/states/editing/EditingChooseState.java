package ru.urfu.logics.mathmechbot.states.editing;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.MathMechBotUserState;
import ru.urfu.logics.mathmechbot.states.DefaultState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;
import ru.urfu.logics.mathmechbot.storages.UserStorage;

/**
 * Состояние в котором пользователь выбирает, какую информацию он хочет изменить.
 */
public final class EditingChooseState implements MathMechBotState {
    private static final String EDITING_FULL_NAME = "full_name";
    private static final String EDITING_YEAR = "year";
    private static final String EDITING_SPECIALITY = "speciality";
    private static final String EDITING_GROUP = "number_of_group";
    private static final String EDITING_MEN = "men";

    private final LocalButton backButton = new LocalButton("Назад", Constants.BACK_COMMAND);
    private final LocalMessage tryAgain = new LocalMessageBuilder().text("Попробуйте снова.").build();

    private final LocalMessage onEnterMessage = new LocalMessageBuilder()
            .text("Что Вы хотите изменить?")
            .buttons(List.of(
                    new LocalButton("ФИО", EDITING_FULL_NAME),
                    new LocalButton("Курс", EDITING_YEAR),
                    new LocalButton("Направление", EDITING_SPECIALITY),
                    new LocalButton("Группа", EDITING_GROUP),
                    new LocalButton("МЕН", EDITING_MEN),
                    backButton))
            .build();

    @Override
    public void processMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                               @NotNull LocalMessage message, @NotNull Bot bot) {
        final UserStorage userStorage = contextCore.getStorage().getUsers();
        switch (message.text()) {
            case Constants.BACK_COMMAND -> backCommandHandler(contextCore, chatId, message, bot);
            case EDITING_FULL_NAME -> {
                userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_FULL_NAME);
                bot.sendMessage(new EditingFullNameState().enterMessage(contextCore, chatId, message, bot), chatId);
            }
            case EDITING_YEAR -> {
                userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_YEAR);
                bot.sendMessage(new EditingYearState().enterMessage(contextCore, chatId, message, bot), chatId);
            }
            case EDITING_SPECIALITY -> {
                userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_SPECIALITY);
                bot.sendMessage(new EditingSpecialityState()
                        .enterMessage(contextCore, chatId, message, bot), chatId);
            }
            case EDITING_GROUP -> {
                userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_GROUP);
                bot.sendMessage(new EditingGroupState().enterMessage(contextCore, chatId, message, bot), chatId);
            }
            case EDITING_MEN -> {
                userStorage.changeUserState(chatId, MathMechBotUserState.EDITING_MEN);
                bot.sendMessage(new EditingMenState().enterMessage(contextCore, chatId, message, bot), chatId);
            }
            case null, default -> bot.sendMessage(tryAgain, chatId);
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                     @NotNull LocalMessage message, @NotNull Bot bot) {
        return onEnterMessage;
    }

    /**
     * Обработка кнопки "назад".
     * @param contextCore логическое ядро
     * @param chatId идентификатор чата
     * @param message текст сообщения
     * @param bot бот в котором пришло сообщение
     */
    private void backCommandHandler(@NotNull MathMechBotCore contextCore, @NotNull Long chatId,
                                    @NotNull LocalMessage message, @NotNull Bot bot) {
        contextCore.getStorage().getUsers().changeUserState(chatId, MathMechBotUserState.DEFAULT);
        bot.sendMessage(new DefaultState().enterMessage(contextCore, chatId, message, bot), chatId);
    }
}
