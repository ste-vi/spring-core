package stevi.spring.context;

import lombok.Getter;
import lombok.Setter;
import stevi.spring.anotations.Bean;
import stevi.spring.anotations.Component;
import stevi.spring.anotations.Configuration;
import stevi.spring.anotations.Lazy;
import stevi.spring.anotations.Service;
import stevi.spring.config.Config;
import stevi.spring.factory.ObjectFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AutowiredApplicationContext implements ApplicationContext {

    @Setter
    private ObjectFactory objectFactory;

    @Getter
    private final Config config;

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    public AutowiredApplicationContext(Config config) {
        this.config = config;
    }

    @Override
    public void postInit() {
        createEagerBeans();
    }

    private void createEagerBeans() {
        createConfigurationBeans();
        createComponentBeans();
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

    private void createComponentBeans() {
        Set<Class<?>> annotatedClasses = config.getReflectionsScanner().getTypesAnnotatedWith(Component.class);
        annotatedClasses.addAll(config.getReflectionsScanner().getTypesAnnotatedWith(Service.class));
        annotatedClasses.stream()
                .filter(aClass -> !aClass.isAnnotationPresent(Lazy.class))
                .forEach(this::getBean);
    }

    @Override
    public <T> T getBeanByName(String beanName) {
        if (cache.containsKey(beanName)) {
            return (T) cache.get(beanName);
        } else {
            throw new RuntimeException("No bean found with provided name: %s".formatted(beanName));
        }
    }

    @Override
    public <T> T getBean(Class<T> aClass) {
        String beanName = getBeanNameFromClass(aClass);
        if (cache.containsKey(beanName)) {
            return (T) cache.get(beanName);
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
            cache.put(aClass.getSimpleName(), object);
        }
    }

    private <T> String getBeanNameFromClass(Class<T> aClass) {
        String classSimpleName = aClass.getSimpleName();
        return formatBeanName(classSimpleName);
    }

    private String formatBeanName(String beanName) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }
}
