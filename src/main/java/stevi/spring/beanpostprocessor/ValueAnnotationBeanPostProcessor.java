package stevi.spring.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.anotations.Value;
import stevi.spring.context.ApplicationContext;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

public class ValueAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, String> propertiesMap;

    public ValueAnnotationBeanPostProcessor() {
        propertiesMap = fetchPropertiesIntoMap();
    }

    @Override
    public void postProcessBeforeInitialization(Object object) {

    }

    @SneakyThrows
    @Override
    public void postProcessAfterInitialization(Object object, ApplicationContext applicationContext) {
        Class<?> objectClass = object.getClass();

        for (var declaredField : objectClass.getDeclaredFields()) {
            Value valueAnnotation = declaredField.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String value = valueAnnotation.value().isEmpty() ? propertiesMap.get(declaredField.getName()) : valueAnnotation.value();
                declaredField.setAccessible(true);
                declaredField.set(object, value);
            }
        }
    }

    @SneakyThrows
    private Map<String, String> fetchPropertiesIntoMap() {
        URI uri = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("application.properties")).toURI();
        Path path = Paths.get(uri);
        List<String> lines = Files.readAllLines(path);
        return lines.stream()
                .map(line -> line.trim().split("="))
                .collect(toMap(array -> array[0], array -> array[1]));
    }
}
