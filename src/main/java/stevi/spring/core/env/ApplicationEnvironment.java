package stevi.spring.core.env;

import stevi.spring.core.anotations.Component;
import stevi.spring.core.env.util.PropertyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ApplicationEnvironment implements Environment {

    private final Map<String, String> propertiesMap = new HashMap<>();
    private String activeProfile = "";

    public ApplicationEnvironment() {
        readApplicationProperties();
        Optional<String> profile = fetchActiveProfile();

        if (profile.isPresent()) {
            activeProfile = profile.get();
            Map<String, String> activeProfileProperties = PropertyUtils.fetchProperties("application-%s.properties".formatted(profile.get()));
            propertiesMap.putAll(activeProfileProperties);
        }
    }

    private void readApplicationProperties() {
        propertiesMap.putAll(PropertyUtils.fetchProperties("application.properties"));
    }

    private Optional<String> fetchActiveProfile() {
        Optional<String> profile = Optional.ofNullable(System.getProperty("spring.profile"));
        if (profile.isEmpty()) {
            profile = propertiesMap
                    .entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().trim().equals("spring.profile.active"))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .map(String::trim);
        }
        return profile;
    }

    @Override
    public String getActiveProfile() {
        return activeProfile;
    }

    @Override
    public String getProperty(String key) {
        return propertiesMap.get(key);
    }
}
