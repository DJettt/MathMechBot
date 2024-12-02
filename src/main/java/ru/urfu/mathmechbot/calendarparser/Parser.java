package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Summary;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Парсер объекта Calendar.
 */
public class Parser {
    private final CalendarBuilder founder = new CalendarBuilder();
    private final Logger logger = LoggerFactory.getLogger(Parser.class);

    /**
     * Получает параметр DateTimeStart (точная дата и время начала ивента) из текущего ивента VEVENT.
     * @param event ивент
     * @return DateTimeStart
     */
    @NotNull
    private LocalDateTime getDateTime(VEvent event) {
        //TODO: при попадании строки DTSTART;VALUE=DATE:20241229 все ломается, хз как фиксить
        if (event.getDateTimeStart().isPresent()) {
            return LocalDateTime.from(event.getDateTimeStart().get().getDate());
        }
        return LocalDateTime.of(1, 1, 1, 0, 0, 0);
    }

    /**
     * Получает точное время начала ивента.
     * @param event ивент
     * @return точное время начала.
     */
    @NotNull
    private LocalTime getTime(VEvent event) {
        //LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
        LocalDateTime eventDateTime = getDateTime(event);
        return eventDateTime.toLocalTime();
    }

    /**
     * Получает точную дату начала ивента.
     * @param event ивент
     * @return точное время начала
     */
    @NotNull
    private LocalDate getDate(VEvent event) {
        //LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
        LocalDateTime eventDateTime = getDateTime(event);
        return eventDateTime.toLocalDate();
    }

    /**
     * Получает описание ивента. В нашем случае - название пары.
     * @param event ивент
     * @return Название пары
     */
    private Optional<String> getSummary(VEvent event) {
        Optional<Summary> summaryOpt = event.getSummary();
        if (summaryOpt.isPresent()) {
            String summary = summaryOpt.get().getValue();
            return Optional.of(summary);
        }
        return Optional.empty();
    }

    /**
     * Получает описание ивента. В нашем случае - Фамилия И.О. преподавателя.
     * @param event ивент
     * @return описание
     */
    private Optional<String> getDescription(VEvent event) {
        Optional<Description> descriptionOptional = event.getDescription();
        if (descriptionOptional.isPresent()) {
            String description = descriptionOptional.get().getValue();
            return Optional.of(description);
        }
        return Optional.empty();
    }

    /**
     * Получает локацию ивента.
     * @param event ивент
     * @return локация
     */
    private Optional<String> getLocation(VEvent event) {
        Optional<Location> locationOpt = event.getLocation();
        if (locationOpt.isPresent()) {
            String location = locationOpt.get().getValue();
            return Optional.of(location);
        }
        return Optional.empty();
    }

    /**
     * Собирает расписание текущей недели из файла .ics в объект Tabletime.
     * @param filename имя файла
     * @return объект Timetable с расписанием
     */
    public Timetable getTimetable(String filename) {
        Calendar cal = founder.getCalendar(filename);
        LocalDate today = LocalDate.now();
        Timetable timetable = new Timetable(today);
        for (Component component : cal.getComponents()) {
            if (component instanceof VEvent event) {
                LocalDate eventDate = getDate(event);
                LocalTime eventTime = getTime(event);
                Optional<String> summary = getSummary(event);
                Optional<String> teacher = getDescription(event);
                Optional<String> location = getLocation(event);
                timetable.add(new Lesson(summary, teacher, location), eventDate, eventTime);
            }
        }
        return timetable;
    }
}
