package stevi.spring.factory;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import stevi.spring.anotations.PostConstruct;
import stevi.spring.aop.ProxyProcessor;
import stevi.spring.beanpostprocessor.BeanPostProcessor;
import stevi.spring.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ObjectFactory {

    private final ApplicationContext applicationContext;
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private final List<ProxyProcessor> proxyProcessors = new ArrayList<>();

    public ObjectFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initBeanPostProcessors(applicationContext);
    }

    @SneakyThrows
    private void initBeanPostProcessors(ApplicationContext applicationContext) {
        Reflections reflectionsScanner = applicationContext.getConfig().getReflectionsScanner();
        for (Class<? extends BeanPostProcessor> aClass : reflectionsScanner.getSubTypesOf(BeanPostProcessor.class)) {
            beanPostProcessors.add(aClass.getDeclaredConstructor().newInstance());
        }
        for (Class<? extends ProxyProcessor> aClass : reflectionsScanner.getSubTypesOf(ProxyProcessor.class)) {
            proxyProcessors.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> implClass) {
        T object = create(implClass);
        configure(object);
        object = wrapWithProxy(implClass, object);

        return object;
    }

    @SneakyThrows
    private <T> T create(Class<T> implClass) {
        return Arrays.stream(implClass.getDeclaredConstructors())
                .filter(constructor -> constructor.getParameterCount() > 0)
                .findFirst()
                .map(constructor -> instantiateConstructorWithParameters(implClass, constructor))
                .orElseGet(() -> instantiateDefaultConstructor(implClass));

    }

    @SneakyThrows
    private <T> T instantiateConstructorWithParameters(Class<T> implClass, Constructor<?> constructor) {
        List<?> instantiatedParameters = Arrays.stream(constructor.getParameterTypes())
                .filter(type -> !type.isPrimitive() && !type.isAssignableFrom(Collection.class) && !type.isAssignableFrom(Map.class))
                .map(applicationContext::getBean)
                .toList();

        if (constructor.getParameterCount() != instantiatedParameters.size()) {
            throw new RuntimeException("Cannot inject beans via constructor of class %s. Not all constructor parameters are beans".formatted(implClass.getName()));
        }

        return (T) constructor.newInstance(instantiatedParameters.toArray());
    }

    @SneakyThrows
    private <T> T instantiateDefaultConstructor(Class<T> implClass) {
        return implClass.getDeclaredConstructor().newInstance();
    }

    private <T> void configure(T object) {
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessBeforeInitialization(object, applicationContext));
        invokeInit(object);
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessAfterInitialization(object, applicationContext));
    }

    @SneakyThrows
    private <T> void invokeInit(T object) {
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(PostConstruct.class)) {
                declaredMethod.invoke(object);
            }
        }
    }

    private <T> T wrapWithProxy(Class<T> implClass, T object) {
        for (ProxyProcessor proxyProcessor : proxyProcessors) {
            object = (T) proxyProcessor.replaceWithProxy(object, implClass);
        }
        return object;
    }

}
