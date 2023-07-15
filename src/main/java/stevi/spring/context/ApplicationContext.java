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
        Class<? extends T> implClass = aClass;
        if (implClass.isInterface()) {
            implClass = config.getImplementation(aClass);
        }
        T object = objectFactory.createObject(implClass);
        if (implClass.isAnnotationPresent(Component.class) || implClass.isAnnotationPresent(Service.class)) {
            cache.put(aClass, object);
        }
        return object;
    }

}
