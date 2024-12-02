package ru.urfu.mathmechbot.jsonparser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Парсер объекта Calendar.
 */
public class Parser {
    private final JSONObjectBuilder jsonObjectBuilder = new JSONObjectBuilder();
    private final static int DEFAULT_LESSON_NUMBER = 0;

    /**
     * Получает порядковый номер пары.
     * @param event ивент
     * @return порядковый номер
     */
    private Optional<Integer> getLessonNumber(JSONObject event) {
        Integer lessonNumber = event.optInt("pairNumber", DEFAULT_LESSON_NUMBER);
        return Optional.of(lessonNumber);
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
     * Собирает расписание текущей недели из файла .ics в объект Tabletime.
     * @param filename имя файла
     * @return объект Timetable с расписанием
     */
    public Timetable getTimetable(String filename) {
        JSONArray jsonArray = jsonObjectBuilder.getJsonObject(filename);
        LocalDate today = LocalDate.now();
        Timetable timetable = new Timetable(today);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject event = jsonArray.getJSONObject(i);
            Optional<LocalDate> eventDate = getDate(event);
            Optional<LocalTime> eventTime = getTime(event);
            Optional<String> title = getTitle(event);
            Optional<String> teacher = getTeacher(event);
            Optional<String> location = getLocation(event);
            Optional<String> format = getLessonFormat(event);
            Optional<Integer> lessonNumber = getLessonNumber(event);
            if (eventDate.isPresent() && lessonNumber.isPresent()) {
                timetable.add(new Lesson(title, teacher, format, location, eventTime),
                        eventDate.get(), lessonNumber.get());
            }
        }
        return timetable;
    }
}
