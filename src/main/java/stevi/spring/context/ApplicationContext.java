package stevi.spring.context;

import lombok.Getter;
import lombok.Setter;
import stevi.spring.anotations.Component;
import stevi.spring.anotations.Service;
import stevi.spring.config.Config;
import stevi.spring.factory.ObjectFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    @Setter
    private ObjectFactory objectFactory;

    @Getter
    private final Map<Class, Object> cache = new ConcurrentHashMap<>();

    @Getter
    private final Config config;

    public ApplicationContext(Config config) {
        this.config = config;
    }

    public <T> T getObect(Class<T> aClass) {
        if (cache.containsKey(aClass)) {
            return (T) cache.get(aClass);
        }

        Class<? extends T> implClass = getImplementationClassIfNeeded(aClass);
        T object = objectFactory.createObject(implClass);
        putObjectIntoCache(aClass, implClass, object);

        return object;
    }

    private <T> Class<? extends T> getImplementationClassIfNeeded(Class<T> aClass) {
        Class<? extends T> implClass = aClass;
        if (implClass.isInterface()) {
            implClass = config.getImplementation(aClass);
        }
        return implClass;
    }

    private <T> void putObjectIntoCache(Class<T> aClass, Class<? extends T> implClass, T object) {
        if (implClass.isAnnotationPresent(Component.class) || implClass.isAnnotationPresent(Service.class)) {
            cache.put(aClass, object);
        }
    }

}
