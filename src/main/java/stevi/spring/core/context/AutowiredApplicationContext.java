package stevi.spring.core.context;

import lombok.Getter;
import lombok.Setter;
import stevi.spring.core.anotations.Bean;
import stevi.spring.core.anotations.Component;
import stevi.spring.core.anotations.Configuration;
import stevi.spring.core.anotations.Lazy;
import stevi.spring.core.anotations.Service;
import stevi.spring.core.config.Config;
import stevi.spring.core.env.ApplicationEnvironment;
import stevi.spring.core.factory.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Implementation of {@link ApplicationContext}
 */
public class AutowiredApplicationContext implements ApplicationContext {

    @Setter
    private BeanFactory beanFactory;

    @Getter
    private final Config config;

    private final BeanCache beanCache;

    public AutowiredApplicationContext(Config config) {
        this.config = config;
        this.beanCache = new BeanCache();
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
        ApplicationEnvironment environment = new ApplicationEnvironment();
        beanCache.put(environment.getClass(), environment);
    }

    private void createConfigurationBeans() {
        Set<Class<?>> classesAnnotatedWithConfiguration = config.getReflectionsScanner().getTypesAnnotatedWith(Configuration.class);
        classesAnnotatedWithConfiguration.stream()
                .filter(configClass -> !configClass.isInterface())
                .forEach(configClass -> {
                    Object configBean = getBean(configClass);
                    for (var declaredMethod : configClass.getDeclaredMethods()) {
                        if (declaredMethod.isAnnotationPresent(Bean.class)) {
                            try {
                                Object createdBean = declaredMethod.invoke(configBean);
                                createdBean = beanFactory.postInitializeBean(createdBean, createdBean.getClass());
                                beanCache.put(declaredMethod.getName(), createdBean);
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
        if (beanCache.contains(beanName)) {
            return (T) beanCache.get(beanName);
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
        Class<? extends T> implClass = getImplementationClass(aClass);
        if (beanCache.contains(implClass)) {
            return (T) beanCache.get(implClass);
        }

        T bean = beanFactory.createBean(implClass);
        putObjectIntoCache(implClass, bean);

        return bean;
    }

    private <T> Class<? extends T> getImplementationClass(Class<T> aClass) {
        Class<? extends T> implClass = aClass;
        if (implClass.isInterface()) {
            implClass = config.getImplementation(aClass);
        }
        return implClass;
    }

    private <T> void putObjectIntoCache(Class<? extends T> implClass, T object) {
        if (implClass.isAnnotationPresent(Component.class) || implClass.isAnnotationPresent(Service.class)) {
            beanCache.put(implClass, object);
        }
    }
}
