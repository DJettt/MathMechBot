package ru.urfu.logics.mathmechbot.states.registration;


import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.mathmechbot.Constants;
import ru.urfu.logics.mathmechbot.MathMechBotCore;
import ru.urfu.logics.mathmechbot.models.userstates.RegistrationUserState;
import ru.urfu.logics.mathmechbot.states.MathMechBotState;


/**
 * Состояние запроса номера группы во время регистрации.
 */
public enum RegistrationGroupState implements MathMechBotState {
    INSTANCE;

    @Override
    public void processMessage(@NotNull MathMechBotCore context, @NotNull LocalMessage msg,
                               long chatId, @NotNull Bot bot) {
        switch (msg.text()) {
            case Constants.BACK_COMMAND -> {
                context.storage.users.changeUserState(chatId, RegistrationUserState.YEAR);

                final LocalMessage message = RegistrationYearState.INSTANCE.enterMessage(context, chatId);
                bot.sendMessage(message, chatId);
            }

            case null -> bot.sendMessage(Constants.TRY_AGAIN, chatId);

            default -> {
                final int maxYear = 5;
                try {
                    final int group = Integer.parseInt(msg.text().trim());
                    if (group >= 1 && group <= maxYear) {
                        context.storage.userEntries.changeUserEntryGroup(chatId, group);
                        context.storage.users.changeUserState(chatId, RegistrationUserState.MEN);
                        bot.sendMessage(RegistrationMenGroupState.INSTANCE.enterMessage(context, chatId), chatId);
                    } else {
                        bot.sendMessage(Constants.TRY_AGAIN, chatId);
                    }
                } catch (NumberFormatException e) {
                    bot.sendMessage(Constants.TRY_AGAIN, chatId);
                }
            }
        }
    }

    @Override
    @NotNull
    public LocalMessage enterMessage(@NotNull MathMechBotCore context, long userId) {
        return new LocalMessageBuilder()
                .text("Какая у Вас группа?")
                .buttons(new ArrayList<>(List.of(
                        new LocalButton("1 группа", "1"),
                        new LocalButton("2 группа", "2"),
                        new LocalButton("3 группа", "3"),
                        new LocalButton("4 группа", "4"),
                        new LocalButton("5 группа", "5"),
                        Constants.BACK_BUTTON)))
                .build();
    }
}
