package ru.urfu.mathmechbot.eventhandlers.editing;

import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * Обрабатывает корректный ввод года обучения во время редактирования.
 */
public final class EditingValidYearEventHandler extends GenericEditingValidEventHandler {
    @Override
    public void handleEvent(ValidInputEvent e) {
        final String text = e.request().message().text();
        assert text != null; // Otherwise it's not valid input

        final int year = Integer.parseInt(text);
        e.request()
                .context()
                .getStorage()
                .getUserEntries()
                .changeUserEntryYear(e.request().user().id(), year);
        super.handleEvent(e);
    }
}
