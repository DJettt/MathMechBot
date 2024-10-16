package ru.urfu.mathmechbot.eventhandlers.editing;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.events.editing.SpecialtyChosenEvent;
import ru.urfu.mathmechbot.logicstates.SpecialtyCheckState;
import ru.urfu.mathmechbot.models.Specialty;

/**
 * Отправляет пользователю сообщение для выбора нового направления подготовки.
 */
public final class EditingSpecialtyChosenEventHandler implements EventHandler<SpecialtyChosenEvent> {
    @Override
    public void handleEvent(SpecialtyChosenEvent e) {
        final int year = e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .orElseThrow()
                .year();

        final List<LocalButton> buttons = new ArrayList<>();
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
