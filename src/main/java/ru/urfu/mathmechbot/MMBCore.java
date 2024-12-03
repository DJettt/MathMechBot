package ru.urfu.mathmechbot;

import java.util.HashSet;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import ru.urfu.bots.Bot;
import ru.urfu.fsm.StateMachine;
import ru.urfu.fsm.StateMachineImpl;
import ru.urfu.logics.LogicCore;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.storages.MathMechStorage;
import ru.urfu.mathmechbot.storages.user.UserStorage;
import ru.urfu.mathmechbot.timetable.TimetableCachedFactory;
import ru.urfu.mathmechbot.timetable.TimetableFactory;


/**
 * <p>Логическое ядро бота, обрабатывающего сообщения
 * Telegram-каналов на предмет упоминания студентов.<p/>
 */
public final class MMBCore implements LogicCore {
    private final MathMechStorage storage;
    private final StateMachine<UserState, Event, EventContext> fsm;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage хранилище данных для логики.
     * @param timetableFactory фабрика расписаний.
     */
    public MMBCore(@NotNull MathMechStorage storage,
                   @NotNull TimetableFactory timetableFactory) {
        this.storage = storage;
        this.fsm = new StateMachineImpl<>(
                new HashSet<>(List.of(UserState.values())),
                UserState.DEFAULT);
        new StateMachineConfig(this.fsm,
                this.storage.getUserEntries(),
                timetableFactory).configure();
    }

    /**
     * <p>Конструктор, использующий в качестве фабрики расписаний --
     * кэшированную фабрику, берущую информация с API УрФУ.</p>
     *
     * @param storage хранилище данных для логики.
     */
    public MMBCore(@NotNull MathMechStorage storage) {
        this(storage, new TimetableCachedFactory());
    }

    @Override
    public void processMessage(@NotNull Long id, @NotNull LocalMessage message, @NotNull Bot bot) {
        final UserStorage userStorage = storage.getUsers();
        final User user = userStorage
                .get(id)
                .orElseGet(() -> {
                    final User newUser = new User(id, UserState.DEFAULT);
                    userStorage.add(newUser);
                    return newUser;
                });

        final UserState currentState = user.currentState();
        fsm.setState(currentState);

        final Event event = currentState
                .messageHandler()
                .processMessage(storage, user, message);

        final EventContext context = new EventContext(user, message, bot);
        final UserState newState = fsm.sendEvent(event, context);
        userStorage.changeUserState(user.id(), newState);
    }
}
