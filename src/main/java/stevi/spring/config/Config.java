package stevi.spring.config;

import org.reflections.Reflections;

public interface Config {

    <T> Class<? extends T> getImplementation(Class<T> interfaceType);

    Reflections getReflectionsScanner();
}
