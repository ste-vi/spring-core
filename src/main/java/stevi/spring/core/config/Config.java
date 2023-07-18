package stevi.spring.core.config;

import org.reflections.Reflections;

public interface Config {

    <T> Class<? extends T> getImplementation(Class<T> interfaceType);

    <T> Class<? extends T> getImplementationByBeanName(Class<T> interfaceType, String beanName);

    Reflections getReflectionsScanner();
}
