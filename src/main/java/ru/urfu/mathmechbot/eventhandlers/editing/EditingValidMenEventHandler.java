package ru.urfu.mathmechbot.eventhandlers.editing;

import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает ввод корректной МЕН-группы во время редактирования данных.
 */
public final class EditingValidMenEventHandler extends GenericEditingValidEventHandler {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryMen(e.request().user().id(), text);

        super.handleEvent(e);
    }
}
