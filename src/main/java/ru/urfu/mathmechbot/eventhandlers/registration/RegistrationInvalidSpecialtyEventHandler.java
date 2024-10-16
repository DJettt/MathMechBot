package ru.urfu.mathmechbot.eventhandlers.registration;

import java.util.ArrayList;
import java.util.List;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.eventhandlers.GenericInvalidInputEventHandler;
import ru.urfu.mathmechbot.events.InvalidInputEvent;
import ru.urfu.mathmechbot.logicstates.SpecialtyCheckState;
import ru.urfu.mathmechbot.models.Specialty;

/**
 * Обрабатывает некорректный ввод направления подготовки.
 */
public final class RegistrationInvalidSpecialtyEventHandler extends GenericInvalidInputEventHandler {
    @Override
    public void handleEvent(InvalidInputEvent e) {
        final int year = e
                .request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .get()
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

        super.handleEvent(e);
        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
