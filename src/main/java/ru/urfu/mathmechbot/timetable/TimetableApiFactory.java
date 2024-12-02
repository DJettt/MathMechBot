package ru.urfu.mathmechbot.timetable;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Достаёт расписание с сайта УрФУ сайта</a> на текущую неделю.</p>
 */
public final class TimetableApiFactory implements TimetableFactory {
    private final static String GROUP_SEARCH_URL_TEMPLATE =
            "https://urfu.ru/api/v2/schedule/groups?search=%s";
    private final static String GROUP_SCHEDULE_URL_TEMPLATE =
            "https://urfu.ru/api/v2/schedule/groups/%d/schedule?date_gte=%s&date_lte=%s";
    private final DateTimeFormatter dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final TimetableUtils utils = new TimetableUtils();

    private final Logger logger = LoggerFactory.getLogger(TimetableCachedFactory.class);

    @Override
    @NotNull
    public Optional<DailyTimetable> getForGroup(@NotNull String men) {
        final Optional<Long> groupId = getGroupId(men);
        if (groupId.isEmpty()) {
            return Optional.empty();
        }

        final Optional<JSONObject> scheduleJsonOptional = getScheduleJson(groupId.get());
        if (scheduleJsonOptional.isPresent()) {
            final JSONObject jsonSchedule = scheduleJsonOptional.get();
            // TODO: jsonSchedule -> Timetable
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
     * <p>Возвращает JSON с расписанием для группы.</p>
     *
     * @param groupId id группы на сайте УрФУ.
     * @return JSON с расписанием для данной группы.
     */
    @NotNull
    private Optional<JSONObject> getScheduleJson(long groupId) {
        final LocalDate today = LocalDate.now();
        final URL scheduleUrl = prepareScheduleUrl(groupId,
                utils.getWeekStartDate(today),
                utils.getWeekEndDate(today));

        try {
            getJsonObject(scheduleUrl);
        } catch (IOException e) {
            logger.error("Couldn't open stream to schedule file", e);
            return Optional.empty();
        }
        JSONObject json;
        try {
            json = getJsonObject(scheduleUrl);
        } catch (JSONException | IOException e) {
            logger.error("Couldn't parse JSON schedule properly for group with id {}", groupId, e);
            return Optional.empty();
        }
        return Optional.of(json);
    }

    /**
     * <p>Возвращает ссылку, по которой можно получить расписание для группы,
     * с заполненными параметрами.</p>
     *
     * @param groupId id группы.
     * @param start   дата, начиная с которой надо вернуть расписание.
     * @param end     дата
     * @return подготовленный ссылку.
     */
    @NotNull
    private URL prepareScheduleUrl(long groupId,
                                   @NotNull LocalDate start,
                                   @NotNull LocalDate end) {
        try {
            return new URI(GROUP_SCHEDULE_URL_TEMPLATE.formatted(
                    groupId,
                    start.format(dateFormatter),
                    end.format(dateFormatter))).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            logger.error("Incorrect URI was passed but that should have happened", e);
            throw new RuntimeException();
        }
    }


    /**
     * <p>Загружает JSON массив с переданного URL.</p>
     *
     * @param url ссылка на сайт с JSON.
     * @return JSON массив.
     * @throws IOException в случае, если строка с URL некорректна.
     */
    @NotNull
    private JSONArray getJsonArray(@NotNull URL url) throws IOException {
        final String json = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new JSONArray(json);
    }

    /**
     * <p>Загружает JSON объект с переданного URL.</p>
     *
     * @param url ссылка на сайт с JSON.
     * @return JSON объект.
     * @throws IOException в случае, если строка с URL некорректна.
     */
    @NotNull
    private JSONObject getJsonObject(@NotNull URL url) throws IOException {
        final String json = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new JSONObject(json);
    }
}
