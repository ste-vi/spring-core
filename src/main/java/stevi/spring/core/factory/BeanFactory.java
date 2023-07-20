package stevi.spring.core.factory;

import lombok.SneakyThrows;
import org.reflections.Reflections;
import stevi.spring.core.anotation.PostConstruct;
import stevi.spring.core.beanpostprocessor.BeanPostProcessor;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.proxy.ProxyProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Class which responsible for creating bean objects and configuring them.
 */
public class BeanFactory {

    private final ApplicationContext applicationContext;
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private final List<ProxyProcessor> proxyProcessors = new ArrayList<>();

    public BeanFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        initProcessors(applicationContext);
    }

    @SneakyThrows
    private void initProcessors(ApplicationContext applicationContext) {
        Reflections reflectionsScanner = applicationContext.getConfig().getReflectionsScanner();
        for (Class<? extends BeanPostProcessor> aClass : reflectionsScanner.getSubTypesOf(BeanPostProcessor.class)) {
            beanPostProcessors.add(aClass.getDeclaredConstructor().newInstance());
        }
        for (Class<? extends ProxyProcessor> aClass : reflectionsScanner.getSubTypesOf(ProxyProcessor.class)) {
            proxyProcessors.add(aClass.getDeclaredConstructor().newInstance());
        }
    }

    /**
     * Creates bean based on given class and configures it.
     *
     * @return ready bean object
     */
    @SneakyThrows
    public <T> T createBean(Class<T> implClass) {
        T bean = create(implClass);
        configure(bean);
        bean = wrapWithProxy(bean, implClass);
        return bean;
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

    private <T> void configure(T bean) {
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessBeforeInitialization(bean, applicationContext));
        invokeInit(bean);
        beanPostProcessors.forEach(beanPostProcessor -> beanPostProcessor.postProcessAfterInitialization(bean, applicationContext));
    }

    @SneakyThrows
    private <T> void invokeInit(T bean) {
        for (Method declaredMethod : bean.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(PostConstruct.class)) {
                declaredMethod.invoke(bean);
            }
        }
    }

    private <T> T wrapWithProxy(T bean, Class<T> implClass) {
        T lastProxy = bean;
        for (ProxyProcessor proxyProcessor : proxyProcessors) {
            lastProxy = (T) proxyProcessor.replaceWithProxy(lastProxy, implClass);
        }
        return lastProxy;
    }

    public Object postInitializeBean(Object bean, Class<?> implClass) {
        configure(bean);
        bean = wrapBeanWithProxy(bean, implClass);
        return bean;
    }

    private Object wrapBeanWithProxy(Object bean, Class<?> implClass) {
        Object lastProxy = bean;
        for (ProxyProcessor proxyProcessor : proxyProcessors) {
            lastProxy = proxyProcessor.replaceWithProxy(lastProxy, implClass);
        }
        return lastProxy;
    }

}
