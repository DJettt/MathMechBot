package ru.urfu.mathmechbot.logicstates;


import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.events.BackEvent;
import ru.urfu.mathmechbot.events.InvalidInputEvent;
import ru.urfu.mathmechbot.events.ValidInputEvent;

/**
 * <p>Обрабатывает три возможных случая.</p>
 *
 * <ul>
 *     <li>Корректные данные -- отправляет в FSM ValidInputEvent.</li>
 *     <li>Некорректные данные -- отправляет в FSM InvalidInputEvent.</li>
 *     <li>Команда возврата -- отправляет в FSM BackEvent.</li>
 * </ul>
 */
public abstract class DataCheckState implements MMBCoreState {
    @Override
    public void processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        final RequestEvent<MMBCore> event = switch (request.message().text()) {
            case Constants.BACK_COMMAND -> new BackEvent(request);
            case null, default -> {
                if (validateData(request)) {
                    yield new ValidInputEvent(request);
                }
                yield new InvalidInputEvent(request);
            }
        };
        request.context().getFsm().dispatch(event);
    }

    /**
     * Проверяет корректность отправленных данных.
     *
     * @param request запрос от ядра с сообщением.
     * @return результат проверки.
     */
    protected abstract boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request);
}
