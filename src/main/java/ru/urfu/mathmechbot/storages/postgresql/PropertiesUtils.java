package ru.urfu.mathmechbot.storages.postgresql;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Работает с properties файлом для db.
 */
public final class PropertiesUtils {
    private static final Properties PROPERTIES = new Properties();

    /**
     * Конструктор.
     */
    PropertiesUtils() {
    }

    /**
     * Подгружаем properties файл в отдельном потоке.
     */
    private void loadProperties() {
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получаем значение по ключу.
     * @param key ключ
     * @return значение
     */
    public String get(String key) {
        this.loadProperties();
        return PROPERTIES.getProperty(key);
    }
}
