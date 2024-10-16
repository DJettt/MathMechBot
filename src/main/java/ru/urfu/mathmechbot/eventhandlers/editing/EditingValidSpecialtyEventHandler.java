package ru.urfu.mathmechbot.eventhandlers.editing;

import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает корректный ввод направления подготовки во время редактирования.
 */
public final class EditingValidSpecialtyEventHandler extends GenericEditingValidEventHandler {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntrySpecialty(e.request().user().id(), text);
        super.handleEvent(e);
    }
}
