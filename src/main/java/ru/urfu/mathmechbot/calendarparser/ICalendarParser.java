package ru.urfu.mathmechbot.calendarparser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Optional;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ICalendarParser {
    private final CalendarBuilder founder = new CalendarBuilder();
    private final Logger logger = LoggerFactory.getLogger(ICalendarParser.class);

    private LocalTime getTime(VEvent event) {
        LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
        return eventDateTime.toLocalTime();
    }

    private LocalDate getDate(VEvent event) {
        LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
        return eventDateTime.toLocalDate();
    }

    private String getSummary(VEvent event) {
        if (event.getProperty("SUMMARY").isPresent()) {
            return event.getProperty("SUMMARY").get().toString();
        } else {
            logger.error("В ивенте нет поля SUMMARY.");
            throw new RuntimeException();
        }
    }

    private Optional<Property> getDescription(VEvent event) {
        if (event.getProperty("DESCRIPTION").isPresent()) {
            return event.getProperty("DESCRIPTION");
        } else {
            return Optional.empty();
        }
    }

    private Optional<Property> getLocation(VEvent event) {
        if (event.getProperty("LOCATION").isPresent()) {
            return event.getProperty("LOCATION");
        } else {
            return Optional.empty();
        }
    }

    public String getTimeable(String filename) {
        Calendar cal = founder.getCalendar(filename);
        String answer = "";
        LocalDateTime today = LocalDateTime.now();
        Temporal date = today.toLocalDate();

        for (Component component : cal.getComponents()) {
            if (component instanceof VEvent event) {

                LocalDate eventDate = null;
                LocalTime eventTime = null;
                if (event.getDateTimeStart().isPresent()) {
                    try {
                        LocalDateTime eventDateTime = LocalDateTime.from(event.getDateTimeStart().get().getDate());
                        eventDate = eventDateTime.toLocalDate();
                        eventTime = eventDateTime.toLocalTime();
                    } catch (RuntimeException e) {
                        System.out.println("Ошибка при получении даты события: " + e.getMessage());
                        System.out.println(event);
                    }
                }

                if (eventDate != null && eventDate.equals(date)) {
                    System.out.println(getSummary(event));
                    System.out.println(getDescription(event));
                    System.out.println(getLocation(event));
                    System.out.println(getDate(event));
                    System.out.println(getTime(event));
                    System.out.println("--------------------------------");
                }
            }
        }
        return answer;
    }
}
