package ru.urfu.mathmechbot.eventhandlers.editing;

import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает корректный ввод номера группы во время редактирования.
 */
public final class EditingValidGroupEventHandler extends GenericEditingValidEventHandler {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryGroup(e.request().user().id(), Integer.parseInt(text));

        super.handleEvent(e);
    }
}
