package stevi.spring.factory;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import stevi.spring.anotations.PostConstruct;
import stevi.spring.beanpostprocessor.BeanPostProcessor;
import stevi.spring.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        T object = create(implClass);
        configure(object);
        invokeInit(object);

        return object;
    }

    private static <T> T create(Class<T> implClass) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return implClass.getDeclaredConstructor().newInstance();
    }

    private <T> void configure(T object) {
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessBeforeInitialization(object));
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessAfterInitialization(object, applicationContext));
    }

    private <T> void invokeInit(T object) throws IllegalAccessException, InvocationTargetException {
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(PostConstruct.class)) {
                declaredMethod.invoke(object);
            }
        }
    }

}
