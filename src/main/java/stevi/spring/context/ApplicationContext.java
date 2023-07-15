package stevi.spring.context;

import stevi.spring.factory.ObjectFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private ObjectFactory objectFactory;
    private Map<Class, Object> cache = new ConcurrentHashMap<>();

    public <T> T getBean(Class<T> type) {
        return null;
    }

}
