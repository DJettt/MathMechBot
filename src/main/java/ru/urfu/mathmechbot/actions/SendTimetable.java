package ru.urfu.mathmechbot.actions;

import java.time.LocalDate;
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
import ru.urfu.mathmechbot.timetable.TimetableFactory;


/**
 * <p>Отправляет пользователю расписание для его группы. Если расписания нет,
 * уведомляет пользователя о возможной ошибке в введённых данных.</p>
 */
public final class SendTimetable implements MMBAction {
    private final static String TIMETABLE_NOT_FOUND = "Расписание для указанной "
            + "группы не найдено. Проверьте корректность введённой группы в формате МЕН.";

    private final Logger logger = LoggerFactory.getLogger(SendTimetable.class);
    private final TimetableFactory timetableFactory;
    private final UserEntryStorage userEntryStorage;

    /**
     * <p>Конструктор.</p>
     *
     * @param storage {@link UserEntryStorage хранилище} для записи.
     */
    public SendTimetable(@NotNull UserEntryStorage storage,
                         @NotNull TimetableFactory timetableFactory) {
        this.userEntryStorage = storage;
        this.timetableFactory = timetableFactory;
    }

    @Override
    public void execute(@NotNull EventContext context) {
        final String text = context.message().text();
        assert text != null; // Otherwise it's not valid input

        final User user = context.user();

        final String userMen = getUserMen(user);
        final Optional<DailyTimetable> t = getDailyTimetable(userMen, LocalDate.now());

        if (t.isPresent()) {
            sendHumanReadableTimetable(t.get(), context);
        } else {
            context.bot().sendMessage(new LocalMessage(TIMETABLE_NOT_FOUND), user.id());
        }
    }

    /**
     * <p>Возвращает расписание для данной группы. Если
     * расписание найти не удалось возвращает пустой Optional.</p>
     *
     * @param men группа в формате МЕН.
     * @param date дата по которой идет поиск.
     * @return Optional с расписанием для данной группы.
     */
    @NotNull
    private Optional<DailyTimetable> getDailyTimetable(@NotNull String men, @NotNull LocalDate date) {
        return timetableFactory.getForGroup(men, date);
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
     * @param dailyTimetable расписание для данного пользователя.
     * @param context   контекст события.
     */
    private void sendHumanReadableTimetable(@NotNull DailyTimetable dailyTimetable,
                                            @NotNull EventContext context) {
        LocalMessage localMessage = new LocalMessage(dailyTimetable.showDailyTimetable());
        context.bot().sendMessage(localMessage, context.user().id());
    }
}
