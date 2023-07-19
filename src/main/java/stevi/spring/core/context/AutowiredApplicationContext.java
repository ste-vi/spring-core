package stevi.spring.core.context;

import lombok.Getter;
import lombok.Setter;
import stevi.spring.core.anotations.Bean;
import stevi.spring.core.anotations.Component;
import stevi.spring.core.anotations.Configuration;
import stevi.spring.core.anotations.Lazy;
import stevi.spring.core.anotations.Service;
import stevi.spring.core.config.Config;
import stevi.spring.core.context.util.BeanNameUtils;
import stevi.spring.core.env.Environment;
import stevi.spring.core.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of {@link ApplicationContext}
 */
public class AutowiredApplicationContext implements ApplicationContext {

    @Setter
    private BeanFactory beanFactory;

    @Getter
    private final Config config;

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public AutowiredApplicationContext(Config config) {
        this.config = config;
    }

    /**
     * Inits method for configuring application context.
     */
    @Override
    public void postInit() {
        createApplicationEnvironment();
        createConfigurationBeans();
        createEagerBeans();
    }

    private void createApplicationEnvironment() {
        getBean(Environment.class);
    }

    private void createConfigurationBeans() {
        Set<Class<?>> classesAnnotatedWithConfiguration = config.getReflectionsScanner().getTypesAnnotatedWith(Configuration.class);
        classesAnnotatedWithConfiguration.stream()
                .filter(configClass -> !configClass.isInterface())
                .map(this::getBean)
                .forEach(configBean -> {
                    for (var declaredMethod : configBean.getClass().getDeclaredMethods()) {
                        if (declaredMethod.isAnnotationPresent(Bean.class)) {
                            try {
                                Object createdBean = declaredMethod.invoke(configBean);
                                cache.put(declaredMethod.getName(), createdBean);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
    }

    private void createEagerBeans() {
        Set<Class<?>> annotatedClasses = config.getReflectionsScanner().getTypesAnnotatedWith(Component.class);
        annotatedClasses.addAll(config.getReflectionsScanner().getTypesAnnotatedWith(Service.class));
        annotatedClasses.stream()
                .filter(aClass -> !aClass.isAnnotationPresent(Lazy.class))
                .forEach(this::getBean);
    }

    /**
     * Fetches bean from cache by a bean name.
     * Throws exception if nothing is found;
     */
    @Override
    public <T> T getBeanByName(String beanName) {
        if (cache.containsKey(beanName)) {
            return (T) cache.get(beanName);
        } else {
            throw new RuntimeException("No bean found with provided name: %s".formatted(beanName));
        }
    }

    /**
     * Fetches bean from cache.
     * Created new bean and sets into cache of not found.
     *
     * @param aClass class to get bean based on
     */
    @Override
    public <T> T getBean(Class<T> aClass) {
        String beanName = BeanNameUtils.getBeanNameFromClass(aClass);
        if (cache.containsKey(beanName)) {
            return (T) cache.get(beanName);
        }

        Class<? extends T> implClass = getImplementationClass(aClass);
        T object = beanFactory.createBean(implClass);
        putObjectIntoCache(aClass, implClass, object);

        return object;
    }

    private <T> Class<? extends T> getImplementationClass(Class<T> aClass) {
        Class<? extends T> implClass = aClass;
        if (implClass.isInterface()) {
            implClass = config.getImplementation(aClass);
        }
        return implClass;
    }

    private <T> void putObjectIntoCache(Class<T> aClass, Class<? extends T> implClass, T object) {
        if (implClass.isAnnotationPresent(Component.class) || implClass.isAnnotationPresent(Service.class)) {
            cache.put(aClass.getSimpleName(), object);
        }
    }
}
