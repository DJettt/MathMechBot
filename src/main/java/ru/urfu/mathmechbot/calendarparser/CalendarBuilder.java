package ru.urfu.mathmechbot.calendarparser;

import java.io.FileInputStream;
import java.io.IOException;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;


public class CalendarBuilder {
    private final static String FILE_PATH = "C:\\Users\\ddget\\IdeaProjects\\MathMechBot\\src\\main"
            + "\\java\\ru\\urfu\\mathmechbot\\calendarparser\\";

    public Calendar getCalendar(String filename) {
        try (FileInputStream fin = new FileInputStream(FILE_PATH + filename + ".ics")) {
            net.fortuna.ical4j.data.CalendarBuilder builder = new net.fortuna.ical4j.data.CalendarBuilder();
            return builder.build(fin);
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (ParserException e) {
            System.err.println("Ошибка парсинга: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
