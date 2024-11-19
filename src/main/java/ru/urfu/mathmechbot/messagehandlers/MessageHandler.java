package ru.urfu.mathmechbot.messagehandlers;

import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorageInterface;

/**
 * <p>Интерфейс обработчика сообщений для MathMechBot.</p>
 *
 * <p><b>Ответственность</b>: создать событие для FSM, основываясь
 * на полученном сообщении. Совершение действий над данными из этого
 * сообщения в ответственность этого класса не входит (для этих целей
 * см. {@link ru.urfu.mathmechbot.actions.MMBAction класс}).</p>
 */
public interface MessageHandler {
    /**
     * <p>Обработчик сообщений. Проверяет содержимое сообщения
     * и определяет, под какое событие оно подходит.</p>
     *
     * <p>Причина передачи хранилища: для принятия решения о том,
     * какое событие следует вернуть, обработчику может потребоваться
     * обращение к хранилищу данных для понимания контекста, в котором
     * сейчас находится пользователь.</p>
     *
     * @param storage хранилище пользовательских записей.
     * @param user    пользователь, для которого обрабатывается сообщение.
     * @param message сообщение с данными.
     * @return событие для FSM.
     */
    @NotNull
    Event processMessage(@NotNull MathMechStorageInterface storage,
                         @NotNull User user,
                         @NotNull LocalMessage message);
}
