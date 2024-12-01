package ru.urfu.mathmechbot.calendarparser;

import java.io.FileInputStream;
import java.io.IOException;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.data.ParserException;

/**
 * Находит файл .ics и собирает его в готовый объект Calendar.
 */
public class CalendarBuilder {
    //TODO поменять на относительный путь
    private final static String FILE_PATH = "src/main/java/ru/urfu/mathmechbot/calendarparser/";

    /**
     * Собирает готовый объект Calendar из файла .ics.
     * @param filename название файла
     * @return Объект Calendar
     */
    //TODO все еще не решена проблема с VTIMEZONE
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
