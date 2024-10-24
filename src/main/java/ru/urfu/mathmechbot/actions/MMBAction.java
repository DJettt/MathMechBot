package ru.urfu.mathmechbot.actions;

import ru.urfu.fsm.Action;
import ru.urfu.mathmechbot.EventContext;

/**
 * <p>Интерфейс Action с предустановленным параметром в виде EventContext.</p>
 *
 * <p>Создан для того, чтобы не таскать в каждом классе этот параметр.</p>
 *
 * <p><b>Ответственность</b>: произвести какое-то одно действие над данными.
 * Валидация данных в ответственность данного класса не входит (для этих целей
 * см. {@link ru.urfu.mathmechbot.validators.MessageValidator класс}).</p>
 */
public interface MMBAction extends Action<EventContext> {
}
