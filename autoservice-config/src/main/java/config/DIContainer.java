package config;

import config.annotation.Component;
import config.annotation.Inject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
@Slf4j
public class DIContainer {

    private final Map<Class<?>, Object> components = new HashMap<>();

    public DIContainer(Class<?>... classes) {
        try {
            for (Class<?> c : classes) {
                if (c.isAnnotationPresent(Component.class)) {
                    Object instance = createInstance(c);
                    log.info("Creating instance of " + c.getSimpleName());
                    components.put(c, instance);
                }
            }

            // 2. Обрабатываем конструкторную инъекцию
            for (Class<?> clazz : components.keySet()) {
                for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
                    if (constructor.isAnnotationPresent(Inject.class)) {

                        constructor.setAccessible(true);

                        Class<?>[] paramTypes = constructor.getParameterTypes();
                        Object[] args = new Object[paramTypes.length];

                        for (int i = 0; i < paramTypes.length; i++) {
                            args[i] = resolveDependency(paramTypes[i]);
                        }

                        Object newObj = constructor.newInstance(args);
                        components.put(clazz, newObj);
                    }
                }
            }

            // 3. Обрабатываем полевую инъекцию
            for (Object obj : components.values()) {
                Class<?> clazz = obj.getClass();

                for (Field field : clazz.getDeclaredFields()) {
                    if (field.isAnnotationPresent(Inject.class)) {
                        field.setAccessible(true);
                        Object dependency = resolveDependency(field.getType());
                        field.set(obj, dependency);
                    }
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object createInstance(Class<?> clazz) throws Exception {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0 || constructor.isAnnotationPresent(Inject.class)) {
                constructor.setAccessible(true);

                Class<?>[] paramTypes = constructor.getParameterTypes();
                if (paramTypes.length == 0) {
                    return constructor.newInstance();
                }

                Object[] params = new Object[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = resolveDependency(paramTypes[i]);
                }

                return constructor.newInstance(params);
            }
        }

        throw new RuntimeException("No suitable constructor found for class " + clazz.getName());
    }

    private Object resolveDependency(Class<?> type) {
        Object dep = components.get(type);
        if (dep != null){
            return dep;
        }
        throw new RuntimeException("No bean found for " + type.getName());
    }

    public <T> T getBean(Class<T> clazz) {
        return (T) components.get(clazz);
    }
}