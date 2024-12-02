package ru.urfu.mathmechbot.actions;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.urfu.logics.localobjects.LocalMessage;
import ru.urfu.mathmechbot.EventContext;
import ru.urfu.mathmechbot.models.User;
import ru.urfu.mathmechbot.models.UserEntry;
import ru.urfu.mathmechbot.storages.userentry.UserEntryStorage;
import ru.urfu.mathmechbot.timetable.DailyTimetable;
import ru.urfu.mathmechbot.timetable.TimetableCachedFactory;
import ru.urfu.mathmechbot.timetable.TimetableFactory;


/**
 * <p>Отправляет пользователю расписание для его группы. Если расписания нет,
 * уведомляет пользователя о возможной ошибке в ввёднных данных.</p>
 */
public final class SendTimetable implements MMBAction {
    private final static String TIMETABLE_NOT_FOUND = "Расписание для указанной "
            + "группы не найдено. Проверьте корректность введённой группы в формате МЕН.";

    private final Logger logger = LoggerFactory.getLogger(SendTimetable.class);
    private final TimetableFactory timetableFactory = new TimetableCachedFactory();
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для записи.
     */
    public SendTimetable(@NotNull UserEntryStorage storage) {
        this.userEntryStorage = storage;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final String text = context.message().text();
        assert text != null; // Otherwise it's not valid input

        final User user = context.user();

        final String userMen = getUserMen(user);
        final Optional<DailyTimetable> t = getTimetable(userMen);

        if (t.isPresent()) {
            sendHumanReadableTimetable(t.get(), context);
        } else {
            context.bot().sendMessage(new LocalMessage(TIMETABLE_NOT_FOUND), user.id());
        }
    }

    /**
     * <p>Возвращает расписание для данной группы. Если
     * расписание найти не удалось возращает пустой Optional.</p>
     *
     * @param men группа в формате МЕН.
     * @return Optional с расписанием для данной группы.
     */
    @NotNull
    private Optional<DailyTimetable> getTimetable(@NotNull String men) {
        return timetableFactory.getForGroup(men);
    }

    /**
     * <p>Находит группу МЕН в пользовательской записи.</p>
     *
     * @param user пользователь, чей МЕН ищем.
     * @return группу в формате МЕН.
     */
    @NotNull
    private String getUserMen(@NotNull User user) {
        final UserEntry userEntry = userEntryStorage
                .get(user.id())
                .orElseThrow(() -> {
                    logger.error("User without asked for timetable");
                    return new RuntimeException();
                });
        if (userEntry.men() == null) {
            logger.error("User without set men asked for timetable");
            throw new RuntimeException();
        }
        return userEntry.men();
    }

    /**
     * <p>Отправляет пользователю сообщение с расписанием.</p>
     *
     * @param timetable расписание для данного пользователя.
     * @param context   контекст события.
     */
    private void sendHumanReadableTimetable(@NotNull DailyTimetable timetable,
                                            @NotNull EventContext context) {
        // TODO
    }
}
