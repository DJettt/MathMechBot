package ru.urfu.mathmechbot.eventhandlers.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.ValidInputEvent;
import ru.urfu.mathmechbot.logicstates.SpecialtyCheckState;
import ru.urfu.mathmechbot.models.Specialty;

/**
 * Обрабатывает корректный ввод года обучения.
 */
public final class RegistrationValidYearEventHandler implements EventHandler<ValidInputEvent> {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final List<LocalButton> buttons = new ArrayList<>();
        final int year = Integer.parseInt(e.request().message().text());
        e.request().context().getStorage().getUserEntries().changeUserEntryYear(
                e.request().user().id(),
                year
        );

        for (Specialty specialty : new SpecialtyCheckState().getAllowedSpecialties(year)) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(Constants.BACK_BUTTON);

        final LocalMessage message = new LocalMessageBuilder()
                .text("На каком направлении?")
                .buttons(buttons)
                .build();

        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
