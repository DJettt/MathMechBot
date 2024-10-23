package ru.urfu.mathmechbot.messagehandlers;


import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.ContextProcessMessageRequest;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.MMBCore;
import ru.urfu.mathmechbot.MMBEvent;
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
public class DataCheckMessageHandler implements MMBMessageHandler {
    private final MessageValidator validator;

    /**
     * <p>Конструктор.</p>
     *
     * @param validator валидатор, который будет использоваться
     *                  для проверки содержимого сообщения.
     */
    public DataCheckMessageHandler(@NotNull MessageValidator validator) {
        this.validator = validator;
    }

    @Override
    public MMBEvent processMessage(@NotNull ContextProcessMessageRequest<MMBCore> request) {
        return switch (request.message().text()) {
            case Constants.BACK_COMMAND -> MMBEvent.BACK;
            case null, default -> {
                if (validateData(request)) {
                    yield MMBEvent.VALID_INPUT;
                }
                yield MMBEvent.INVALID_INPUT;
            }
        };
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
