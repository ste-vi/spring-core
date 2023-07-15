package stevi.spring.factory;

import lombok.SneakyThrows;
import stevi.spring.beanpostprocessor.BeanPostProcessor;
import stevi.spring.config.Config;
import stevi.spring.config.DefaultConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectFactory {

    private static final ObjectFactory instance = new ObjectFactory();
    private final Config config;
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public static ObjectFactory getInstance() {
        return instance;
    }

    @SneakyThrows
    private ObjectFactory() {
        config = new DefaultConfig("stevi.spring", Map.of());
        for (Class<? extends BeanPostProcessor> aClass : config.getReflectionsScanner().getSubTypesOf(BeanPostProcessor.class)) {
            beanPostProcessors.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        Class<? extends T> typeImpl = type;
        if (typeImpl.isInterface()) {
            typeImpl = config.getImplementation(type);
        }
        T object = typeImpl.getDeclaredConstructor().newInstance();

        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessBeforeInitialization(object));
        // some custom bean init methods..
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessAfterInitialization(object));

        return object;
    }

}
