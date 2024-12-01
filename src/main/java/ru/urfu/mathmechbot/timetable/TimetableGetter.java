package ru.urfu.mathmechbot.timetable;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Достаёт расписание с <a href="https://urfu.ru/ru/students/study/schedule/#/groups">
 * сайта</a>.</p>
 */
public final class TimetableGetter {
    private final static String GROUP_SEARCH_URL_TEMPLATE =
            "https://urfu.ru/api/v2/schedule/groups?search=%s";
    private final static String GROUP_SCHEDULE_URL_TEMPLATE =
            "https://urfu.ru/api/v2/schedule/groups/%d/schedule.ics";

    private final Logger logger = LoggerFactory.getLogger(TimetableGetter.class);
    private final Locale ruLocale = new Locale.Builder()
            .setLanguage("ru").setRegion("RU").build();

    private final ConcurrentMap<String, DailyTimetable> cache = new ConcurrentHashMap<>();

    /**
     * <p>Достаёт расписание для переданной группы. Если расписания не нашлось,
     * возвращает пустой Optional.</p>
     *
     * @param men номер группы, чьё расписание ищется.
     * @return Optional с расписанием.
     */
    @NotNull
    public Optional<DailyTimetable> getForGroup(@NotNull String men) {
        final DailyTimetable timetable = cache.get(men);
        if (timetable != null && !isTimetableExpired(timetable)) {
            return Optional.of(timetable);
        }

        final Optional<DailyTimetable> newTimetable = getFromApi(men);
        newTimetable.ifPresent(t -> cache.put(men, t));
        return newTimetable;
    }

    /**
     * <p>Ищет расписание на <a href="https://urfu.ru/ru/students/study/schedule/#/groups">
     * *     сайте</a> для данной группы.</p>
     *
     * @param men группа, для которой ищется расписание.
     * @return расписание для данной группы.
     */
    @NotNull
    private Optional<DailyTimetable> getFromApi(@NotNull String men) {
        final Optional<Long> groupId = getGroupId(men);
        if (groupId.isEmpty()) {
            return Optional.empty();
        }

        final Optional<InputStream> scheduleStreamOptional = getScheduleStream(groupId.get());
        if (scheduleStreamOptional.isPresent()) {
            final InputStream scheduleStream = scheduleStreamOptional.get();
            // TODO: scheduleStream -> Timetable
            return Optional.empty();
        }

        return Optional.empty();
    }

    /**
     * <p>Находит id группы на основе переданного номера группы.</p>
     *
     * @param men номер группы в формате МЕН.
     * @return id группы.
     */
    @NotNull
    private Optional<Long> getGroupId(@NotNull String men) {
        final String encodedMen = URLEncoder.encode(men, StandardCharsets.UTF_8);
        final String url = GROUP_SEARCH_URL_TEMPLATE.formatted(encodedMen);

        JSONArray groupJson;
        try {
            groupJson = getJsonArray(new URI(url).toURL());
        } catch (URISyntaxException | IOException e) {
            logger.error("Error during downloading JSON", e);
            return Optional.empty();
        }

        long groupId;
        try {
            groupId = groupJson.getJSONObject(0).getLong("id");
        } catch (JSONException e) {
            logger.error("Couldn't parse JSON properly for group {}", men, e);
            return Optional.empty();
        }
        return Optional.of(groupId);
    }

    /**
     * <p>Возвращает файл с расписание для группы.</p>
     *
     * @param groupId id группы на сайте УрФУ.
     * @return файл с расписанием iCalendar.
     */
    @NotNull
    private Optional<InputStream> getScheduleStream(long groupId) {
        URL scheduleUrl;
        try {
            scheduleUrl = new URI(GROUP_SCHEDULE_URL_TEMPLATE.formatted(groupId)).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            logger.error("Incorrect URI was passed but that should have happened", e);
            throw new RuntimeException();
        }

        try {
            final InputStream in = new BufferedInputStream(scheduleUrl.openStream());
            return Optional.of(in);
        } catch (IOException e) {
            logger.error("Couldn't open stream to schedule file", e);
            return Optional.empty();
        }
    }

    /**
     * <p>Проверяет, истёк ли срок актуальности расписания. Расписание должно
     * перестаёт быть актульным, если с момента его создания закончилась одна неделя,
     * то есть прошло воскресенье той недели, на которой расписание было создано.</p>
     *
     * @param timetable расписание.
     * @return результат проверки.
     */
    private boolean isTimetableExpired(@NotNull DailyTimetable timetable) {
        final int timetableWeek = timetable.date()
                .get(WeekFields.of(ruLocale).weekOfYear());
        final int currentWeek = LocalDate.now()
                .get(WeekFields.of(ruLocale).weekOfYear());
        return timetableWeek != currentWeek;
    }

    /**
     * <p>Загружает JSON с переданного URL.</p>
     * @param url ссылка на сайт с JSON.
     * @return JSON массив.
     * @throws IOException в случае, если строка с URL некорректна.
     */
    @NotNull
    private JSONArray getJsonArray(@NotNull URL url) throws IOException {
        final String json = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new JSONArray(json);
    }
}
