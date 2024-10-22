package ru.urfu.mathmechbot.logicstates;


import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.RequestEvent;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.events.BackEvent;
import ru.urfu.mathmechbot.events.InvalidInputEvent;
import ru.urfu.mathmechbot.events.ValidInputEvent;
import ru.urfu.mathmechbot.validators.MessageValidator;

/**
 * <p>Обрабатывает три возможных случая.</p>
 *
 * <ul>
 *     <li>Корректные данные -- отправляет в FSM ValidInputEvent.</li>
 *     <li>Некорректные данные -- отправляет в FSM InvalidInputEvent.</li>
 *     <li>Команда возврата -- отправляет в FSM BackEvent.</li>
 * </ul>
 */
public class DataCheckState implements MMBCoreState {
    private final MessageValidator validator;

    /**
     * <p>Конструктор.</p>
     *
     * @param validator валидатор, который будет использоваться
     *                  для проверки содержимого сообщения.
     */
    public DataCheckState(@NotNull MessageValidator validator) {
        this.validator = validator;
    }

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
        request.context().getFsm().sendEvent(event);
    }

    /**
     * <p>Проверяет корректность отправленных данных.</p>
     *
     * @param request запрос от ядра с сообщением.
     * @return результат проверки.
     */
    public boolean validateData(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return validator.validateMessageContent(request.message());
    }
}
