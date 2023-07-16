package stevi.spring.config;

import lombok.Getter;
import org.reflections.Reflections;
import stevi.spring.anotations.Primary;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

public class DefaultConfig implements Config {

    @Getter
    private final Reflections reflectionsScanner;

    public DefaultConfig(String basePackage) {
        this.reflectionsScanner = new Reflections(basePackage);
    }

    /**
     * Gets an implementations of an interface.
     * It there is more than one implementation we use the one annotated with {@link stevi.spring.anotations.Primary} annotation
     */
    @Override
    public <T> Class<? extends T> getImplementation(Class<T> interfaceType) {
        Set<Class<? extends T>> implementations = reflectionsScanner.getSubTypesOf(interfaceType);
        if (implementations.size() == 1) {
            return implementations.iterator().next();
        } else if (implementations.isEmpty()) {
            throw new RuntimeException("No implementations find for the interface %s".formatted(interfaceType.getName()));
        } else {
            return getPrimaryImplementation(interfaceType, implementations);
        }
    }

    private <T> Class<? extends T> getPrimaryImplementation(Class<T> interfaceType, Set<Class<? extends T>> implementations) {
        Set<Class<? extends T>> primaryImplementations = getImplementationsWithAnnotation(implementations, Primary.class);
        if (primaryImplementations.size() == 1) {
            return primaryImplementations.iterator().next();
        } else if (primaryImplementations.size() > 1) {
            throw new RuntimeException("There are more then one implementation of interface %s annotated with @Primary".formatted(interfaceType.getName()));
        } else {
            throw new RuntimeException("No primary implementations find for the interface %s. Either set specific one instead of interface or mark one of the implementations with @Primary".formatted(interfaceType.getName()));
        }
    }

    private <T> Set<Class<? extends T>> getImplementationsWithAnnotation(Set<Class<? extends T>> types,
                                                                         Class<? extends Annotation> annotationClass) {
        return types
                .stream()
                .filter(implementation -> implementation.isAnnotationPresent(annotationClass))
                .collect(Collectors.toSet());
    }

    /**
     * Gets an implementations of an interface by a simple class name.
     */
    @Override
    public <T> Class<? extends T> getImplementationByBeanName(Class<T> interfaceType, String beanName) {
        if (beanName.isBlank()) {
            throw new RuntimeException("Bean name is blank provided in @Qualifier for interface type %s".formatted(interfaceType));
        }
        Set<Class<? extends T>> types = reflectionsScanner.getSubTypesOf(interfaceType);
        return types.stream()
                .filter(implementation -> implementation.getSimpleName().equals(beanName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No bean find with the qualified name %s".formatted(beanName)));
    }
}
