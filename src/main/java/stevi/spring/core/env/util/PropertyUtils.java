package stevi.spring.core.env.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

@Slf4j
public final class PropertyUtils {

    public static Map<String, String> fetchProperties(String fileName) {
        try {
            URI uri = Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(fileName)).toURI();
            Path path = Paths.get(uri);
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .filter(line -> !line.isBlank())
                    .map(line -> line.trim().split("="))
                    .collect(toMap(array -> array[0].trim(), array -> array[1].trim()));
        } catch (URISyntaxException exception) {
            log.warn("No property file is found with file name: {}. Please create the file in resources, or else no properties will be fetched", fileName);
            return new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
