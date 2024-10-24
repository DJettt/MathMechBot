package ru.urfu.mathmechbot.messagehandlers;


import org.jetbrains.annotations.NotNull;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.Constants;
import ru.urfu.mathmechbot.Event;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorage;
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
public class DataCheckMessageHandler implements MessageHandler {
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
    public Event processMessage(@NotNull MathMechStorage storage,
                                @NotNull User user,
                                @NotNull LocalMessage message) {
        return switch (message.text()) {
            case Constants.BACK_COMMAND -> Event.BACK;
            case null, default -> {
                if (validateData(storage, user, message)) {
                    yield Event.VALID_INPUT;
                }
                yield Event.INVALID_INPUT;
            }
        };
    }

    /**
     * <p>Проверяет корректность отправленных данных.</p>
     *
     * @param storage хранилище пользовательских записей.
     * @param user пользователь, для которого проверяется валидность данных.
     * @param message сообщение с данными.
     * @return результат проверки.
     */
    public boolean validateData(@NotNull MathMechStorage storage,
                                @NotNull User user,
                                @NotNull LocalMessage message) {
        return validator.validateMessageContent(storage, user, message);
    }
}
