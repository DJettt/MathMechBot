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

public class Parser {
    private final CalendarBuilder founder = new CalendarBuilder();
    private final Logger logger = LoggerFactory.getLogger(Parser.class);

    @NotNull
    private LocalDateTime getDateTime(VEvent event) {
        //TODO: при попадании строки DTSTART;VALUE=DATE:20241229 все ломается, хз как фиксить
        if (event.getDateTimeStart().isPresent()) {
            return LocalDateTime.from(event.getDateTimeStart().get().getDate());
        } else {
            if (event.getDateTimeEnd().isPresent()) {
                return LocalDateTime.from(event.getDateTimeEnd().get().getDate()).minusMinutes(90);
            } else {
                return LocalDateTime.of(1, 1, 1, 0, 0);
            }
        }
    }

    @NotNull
    private LocalTime getTime(VEvent event) {
        //LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
        LocalDateTime eventDateTime = getDateTime(event);
        return eventDateTime.toLocalTime();
    }

    @NotNull
    private LocalDate getDate(VEvent event) {
        //TODO считать с конца пары
        //LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
        LocalDateTime eventDateTime = getDateTime(event);
        return eventDateTime.toLocalDate();
    }

    private Optional<String> getSummary(VEvent event) {
        Optional<Summary> summaryOpt = event.getSummary();
        if (summaryOpt.isPresent()) {
            String summary = summaryOpt.get().getValue();
            return Optional.of(summary);
        }
        return Optional.empty();
    }

    private Optional<String> getDescription(VEvent event) {
        Optional<Description> descriptionOptional = event.getDescription();
        if (descriptionOptional.isPresent()) {
            String description = descriptionOptional.get().getValue();
            return Optional.of(description);
        }
        return Optional.empty();
    }

    private Optional<String> getLocation(VEvent event) {
        Optional<Location> locationOpt = event.getLocation();
        if (locationOpt.isPresent()) {
            String location = locationOpt.get().getValue();
            return Optional.of(location);
        }
        return Optional.empty();
    }

    public Timetable getTimeable(String filename) {
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
