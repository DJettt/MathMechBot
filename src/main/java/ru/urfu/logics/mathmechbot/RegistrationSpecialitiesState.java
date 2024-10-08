package ru.urfu.logics.mathmechbot;

import ru.urfu.bots.Bot;
import ru.urfu.enums.RegistrationProcessState;
import ru.urfu.enums.Specialty;
import ru.urfu.localobjects.LocalButton;
import ru.urfu.localobjects.LocalMessage;
import ru.urfu.localobjects.LocalMessageBuilder;
import ru.urfu.logics.State;

import java.util.ArrayList;
import java.util.List;


public class RegistrationSpecialitiesState extends MathMechBotState {
    public RegistrationSpecialitiesState(MathMechBotCore context) {
        super(context);
    }

    protected List<Specialty> allowedSpecialties() {
        return new ArrayList<>() {
        };
    }

    @Override
    public void processMessage(LocalMessage msg, long chatId, Bot bot) {
        switch (msg.text()) {
            case BACK_COMMAND -> {
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.YEAR);
                final State newState = new RegistrationYearState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }

            case null -> bot.sendMessage(TRY_AGAIN, chatId);

            default -> {
                if (!allowedSpecialties().stream().map(Specialty::getAbbreviation).toList().contains(msg.text())) {
                    bot.sendMessage(TRY_AGAIN, chatId);
                    return;
                }

                context.userEntries.getById(chatId).setSpecialty(msg.text());
                context.users.getById(chatId).setCurrentState(RegistrationProcessState.GROUP);
                final State newState = new RegistrationGroupState(context);
                newState.onEnter(msg, chatId, bot);
                context.changeState(newState);
            }
        }
    }

    @Override
    public void onEnter(LocalMessage msg, long chatId, Bot bot) {
        List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : allowedSpecialties()) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(new LocalButton("Назад", BACK_COMMAND));

        final LocalMessage message = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(buttons)
                .build();

        bot.sendMessage(message, chatId);
    }
}
