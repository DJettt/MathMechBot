package ru.urfu.mathmechbot.jsonparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Собирает JSON объект из JSON файла.
 */
public class JSONObjectBuilder {
    private final Logger logger = LoggerFactory.getLogger(JSONObjectBuilder.class);
    private final static String FILE_PATH = "C:\\Users\\ddget\\IdeaProjects\\testingICalendarParser\\src\\main\\java"
            + "\\org\\example\\";

    /**
     * Собирает JSON объект из JSON файла.
     * @param filename имя файла
     * @return json объект
     */
    public JSONArray getJsonObject(String filename) {
        try {
            String content = Files.readString(Paths.get(FILE_PATH + filename));
            return new JSONObject(content).getJSONArray("events");
        } catch (IOException e) {
            logger.error("Ошибка при считывании файла.", e);
            throw new RuntimeException(e);
        }
    }
}
