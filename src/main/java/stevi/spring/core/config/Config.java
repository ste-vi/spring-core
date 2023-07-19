package stevi.spring.core.config;

import org.reflections.Reflections;

/**
 * Interface that defines methods for looking up for bean implementations of interfaces.
 * Can find implementations annotated {@link stevi.spring.core.anotations.Primary} annotation.
 * Can find implementations by class names.
 */
public interface Config {

    /**
     * Gets an implementations of an interface.
     * It there is more than one implementation we use the one annotated with {@link stevi.spring.core.anotations.Primary} annotation
     */
    <T> Class<? extends T> getImplementation(Class<T> interfaceType);

    /**
     * Gets an implementations of an interface by a simple class name.
     * Used for {{@link stevi.spring.core.anotations.Qualifier annotation}}
     */
    <T> Class<? extends T> getImplementationByBeanName(Class<T> interfaceType, String beanName);

    /**
     * Returns reflection scanner which might be needed to find some classes elsewhere in the code.
     */
    Reflections getReflectionsScanner();
}
