package stevi.spring.factory;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import stevi.spring.beanpostprocessor.BeanPostProcessor;
import stevi.spring.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ObjectFactory {

    private final ApplicationContext applicationContext;
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @SneakyThrows
    public ObjectFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        Reflections reflectionsScanner = applicationContext.getConfig().getReflectionsScanner();
        for (Class<? extends BeanPostProcessor> aClass : reflectionsScanner.getSubTypesOf(BeanPostProcessor.class)) {
            beanPostProcessors.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> implClass) {
        T object = implClass.getDeclaredConstructor().newInstance();

        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessBeforeInitialization(object));
        // some custom bean init methods..
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessAfterInitialization(object, applicationContext));

        return object;
    }

}
