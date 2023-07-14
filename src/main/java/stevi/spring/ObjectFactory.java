package stevi.spring;

import lombok.SneakyThrows;
import stevi.spring.anotations.Value;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ObjectFactory {

    private static final ObjectFactory instance = new ObjectFactory();
    private final Config config;

    public static ObjectFactory getInstance() {
        return instance;
    }

    private ObjectFactory() {
        config = new DefaultConfig("stevi.spring", Map.of());
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        Class<? extends T> typeImpl = type;
        if (typeImpl.isInterface()) {
            typeImpl = config.getImplementation(type);
        }
        T object = typeImpl.getDeclaredConstructor().newInstance();

        Map<String, String> propertiesMap = getPropertiesMap(typeImpl);

        for (var declaredField : typeImpl.getDeclaredFields()) {
            Value valueAnnotation = declaredField.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String value = valueAnnotation.value().isEmpty() ? propertiesMap.get(declaredField.getName()) : valueAnnotation.value();
                declaredField.setAccessible(true);
                declaredField.set(object, value);
            }
        }

        return object;
    }

    @SneakyThrows
    private <T> Map<String, String> getPropertiesMap(Class<? extends T> typeImpl) {
        URI uri = typeImpl.getClassLoader().getResource("application.properties").toURI();
        Path path = Paths.get(uri);
        List<String> lines = Files.readAllLines(path);
        return lines.stream()
                .map(line -> line.trim().split("="))
                .collect(toMap(array -> array[0], array -> array[1]));
    }
}
