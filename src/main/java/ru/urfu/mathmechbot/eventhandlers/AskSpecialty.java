package ru.urfu.mathmechbot.eventhandlers;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.fsm.EventHandler;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.LocalButton;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.logics.localobjects.LocalMessageBuilder;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.logicstates.SpecialtyCheckState;
import ru.urfu.mathmechbot.models.Specialty;
import ru.urfu.mathmechbot.models.UserEntry;

/**
 * <p>Отправляет пользователю сообщение с вариантами
 * выбора направления подготовки (на основе года обучения).</p>
 */
public final class AskSpecialty implements EventHandler<RequestEvent<MMBCore>> {
    private final Logger logger = LoggerFactory.getLogger(AskSpecialty.class);

    @Override
    public void handleEvent(RequestEvent<MMBCore> e) {
        final UserEntry userEntry = e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .get(e.request().user().id())
                .orElseThrow();

        if (userEntry.year() == null) {
            logger.error("User entry doesn't contain year but it should");
            return;
        }

        final List<LocalButton> buttons = new ArrayList<>();
        for (Specialty specialty : new SpecialtyCheckState().getAllowedSpecialties(userEntry.year())) {
            buttons.add(new LocalButton(specialty.getAbbreviation(), specialty.getAbbreviation()));
        }
        buttons.add(Constants.BACK_BUTTON);

        final LocalMessage message = new LocalMessageBuilder()
                .text("""
                        На каком направлении?
                        Если Вы не видите свое направление, то, возможно, Вы выбрали не тот курс.""")
                .buttons(buttons)
                .build();

        e.request().bot().sendMessage(message, e.request().user().id());
    }
}
