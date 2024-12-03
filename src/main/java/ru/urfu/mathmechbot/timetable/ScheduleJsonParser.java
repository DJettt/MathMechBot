package ru.urfu.mathmechbot.timetable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Парсер JSON объекта расписания.
 */
public final class ScheduleJsonParser {
    private final static int DEFAULT_LESSON_NUMBER = 0;

    /**
     * Получает порядковый номер пары.
     * @param event ивент
     * @return порядковый номер
     */
    private int getLessonNumber(JSONObject event) {
        return event.optInt("pairNumber", DEFAULT_LESSON_NUMBER);
    }

    /**
     * Получает точное время начала ивента.
     * @param event ивент
     * @return точное время начала.
     */
    private Optional<LocalTime> getTime(JSONObject event) {
        String time = event.optString("timeBegin", null);
        try {
            return Optional.of(LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Получает точную дату начала ивента.
     * @param event ивент
     * @return точное время начала
     */
    private Optional<LocalDate> getDate(JSONObject event) {
        String date = event.optString("date", null);
        try {
            return Optional.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    /**
     * Получает описание ивента. В нашем случае - название пары.
     * @param event ивент
     * @return Название пары
     */
    private Optional<String> getTitle(JSONObject event) {
        String title = event.optString("title", null);
        return Optional.ofNullable(title);
    }

    /**
     * Получает описание ивента. В нашем случае - Фамилия И.О. преподавателя.
     * @param event ивент
     * @return описание
     */
    private Optional<String> getTeacher(JSONObject event) {
        String teacher = event.optString("teacherName", null);
        return Optional.ofNullable(teacher);
    }

    /**
     * Получает локацию ивента.
     * @param event ивент
     * @return локация
     */
    private Optional<String> getLocation(JSONObject event) {
        String classroom = event.optString("auditoryTitle", null);
        String location = event.optString("auditoryLocation", null);
        if (classroom == null && location == null) {
            return Optional.empty();
        } else if (classroom == null) {
            return Optional.of(location);
        } else if (location == null) {
            return Optional.of(classroom);
        } else {
            return Optional.of(classroom + " " + location);
        }
    }

    /**
     * Получает формат пары.
     * @param event ивент
     * @return формат
     */
    private Optional<String> getLessonFormat(JSONObject event) {
        String format = event.getString("loadType");
        return Optional.of(format);
    }

    /**
     * Вытаскивает все ивенты из JSON объекта расписания.
     * @param jsonObject json объект расписания
     * @return json массив с ивентами
     */
    private JSONArray extractEvents(JSONObject jsonObject) {
        return jsonObject.getJSONArray("events");
    }

    /**
     * Собирает объект WeeklyTimetable из JSON объекта с расписанием.
     * @param jsonObject объект с расписанием
     * @return объект WeeklyTimetable с расписанием
     */
    public Map<LocalDate, DailyTimetable> parseScheduleJson(JSONObject jsonObject) {
        Map<LocalDate, DailyTimetable> dailyTimetables = new HashMap<>();
        JSONArray jsonArray = extractEvents(jsonObject);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject event = jsonArray.getJSONObject(i);
            Optional<LocalDate> eventDate = getDate(event);
            Optional<LocalTime> eventTime = getTime(event);
            Optional<String> title = getTitle(event);
            Optional<String> teacher = getTeacher(event);
            Optional<String> location = getLocation(event);
            Optional<String> format = getLessonFormat(event);
            int lessonNumber = getLessonNumber(event);
            if (eventDate.isPresent() && title.isPresent()) {
                if (dailyTimetables.containsKey(eventDate.get())) {
                    Lesson lesson = new Lesson(title.get(), teacher, format, location, eventTime);
                    dailyTimetables.get(eventDate.get()).add(lesson, lessonNumber);
                } else {
                    dailyTimetables.put(eventDate.get(), new DailyTimetable(eventDate.get()));
                    Lesson lesson = new Lesson(title.get(), teacher, format, location, eventTime);
                    dailyTimetables.get(eventDate.get()).add(lesson, lessonNumber);
                }
            }
        }
        return dailyTimetables;
    }
}
