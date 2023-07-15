package stevi.spring.config;

import lombok.Getter;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

public class DefaultConfig implements Config {

    @Getter
    private final Reflections reflectionsScanner;

    private Map<Class, Class> ifcToImplMap;

    public DefaultConfig(String basePackage, Map<Class, Class> ifcToImplMap) {
        this.reflectionsScanner = new Reflections(basePackage);
        this.ifcToImplMap = ifcToImplMap;
    }

    @Override
    public <T> Class<? extends T> getImplementation(Class<T> interfaceType) {
        return ifcToImplMap.computeIfAbsent(interfaceType, aClass -> {
            Set<Class<? extends T>> types = reflectionsScanner.getSubTypesOf(interfaceType);
            if (types.size() != 1) {
                throw new RuntimeException();
            }
            return types.iterator().next();
        });
    }
}
