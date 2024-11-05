package ru.urfu.database;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Работает с properties файлом для db.
 */
public final class PropertiesUtils {
    private static final Properties PROPERTIES = new Properties();
    static {
        loadProperties();
    }

    /**
     * Конструктор.
     */
    private PropertiesUtils() {
    }

    /**
     * Получаем значение по ключу.
     * @param key ключ
     * @return значение
     */
    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    /**
     * Подгружаем properties файл в отдельном потоке.
     */
    private static void loadProperties() {
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
