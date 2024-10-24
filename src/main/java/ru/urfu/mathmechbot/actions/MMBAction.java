package ru.urfu.mathmechbot.actions;

import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.EventContext;

/**
 * <p>Интерфейс Action с предустановленным параметром в виде EventContext.</p>
 *
 * <p>Создан для того, чтобы не таскать в каждом классе этот параметр.</p>
 */
public interface MMBAction extends Action<EventContext> {
}
