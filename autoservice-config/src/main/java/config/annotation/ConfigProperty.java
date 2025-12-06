package config.annotation;

import config.enums.ConfigPropertyType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigProperty {
    String configFileName() default "application.properties";
    String propertyName() default "";
    ConfigPropertyType type() default ConfigPropertyType.BOOLEAN;
}
