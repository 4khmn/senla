package config;

import config.annotation.ConfigProperty;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigLoader {
    public static void load(Object obj) {
        Class<?> clazz = obj.getClass();

        try (InputStream is = obj.getClass().getClassLoader().getResourceAsStream("autoservice.properties")) {
            if (is == null){
                throw new RuntimeException("Не найден autoservice.properties");
            }

            Properties props = new Properties();
            props.load(is);

            for (Field field : clazz.getDeclaredFields()) {
                ConfigProperty annotation = field.getAnnotation(ConfigProperty.class);
                if (annotation == null){
                    continue;
                }

                String key = annotation.propertyName();
                String value = props.getProperty(key);
                if (value == null){
                    continue;
                }

                field.setAccessible(true);
                if (field.getType() == boolean.class) {
                    field.setBoolean(obj, Boolean.parseBoolean(value));
                } else if (field.getType() == String.class) {
                    field.set(obj, value);
                }
            }
        } catch (IOException | IllegalAccessException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }
}
