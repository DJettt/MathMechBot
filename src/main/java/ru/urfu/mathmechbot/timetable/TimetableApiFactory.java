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
import java.util.Optional;
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
public final class TimetableApiFactory implements TimetableFactory {
    private final static String GROUP_SEARCH_URL_TEMPLATE =
            "https://urfu.ru/api/v2/schedule/groups?search=%s";
    private final static String GROUP_SCHEDULE_URL_TEMPLATE =
            "https://urfu.ru/api/v2/schedule/groups/%d/schedule.ics";

    private final Logger logger = LoggerFactory.getLogger(TimetableCachedFactory.class);

    @Override
    @NotNull
    public Optional<DailyTimetable> getForGroup(@NotNull String men) {
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
