package stevi.spring.core.env;

import stevi.spring.core.anotations.Component;
import stevi.spring.core.env.util.PropertyUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Actual implementation of {@link Environment}.
 * Hols the profile and the property map of the current application.
 */
@Component
public class ApplicationEnvironment implements Environment {

    private final Map<String, String> propertiesMap = new HashMap<>();
    private String activeProfile = "";

    public ApplicationEnvironment() {
        readApplicationProperties();
        Optional<String> profile = fetchActiveProfile();

        if (profile.isPresent()) {
            activeProfile = profile.get();
            Map<String, String> activeProfileProperties = PropertyUtils.fetchProperties(CUSTOM_ENV_APPLICATION_PROPERTIES_FILE.formatted(profile.get()));
            propertiesMap.putAll(activeProfileProperties);
        }
    }

    private void readApplicationProperties() {
        propertiesMap.putAll(PropertyUtils.fetchProperties(APPLICATION_PROPERTIES_FILE));
    }

    private Optional<String> fetchActiveProfile() {
        Optional<String> profile = Optional.ofNullable(System.getenv(ACTIVE_PROFILE_SYSTEM_PROPERTY_NAME));
        if (profile.isEmpty()) {
            profile = propertiesMap
                    .entrySet()
                    .stream()
                    .filter(entry -> !entry.getKey().isEmpty())
                    .filter(entry -> ACTIVE_PROFILE_APPLICATION_PROPERTY_NAME.equals(entry.getKey().trim()))
                    .findFirst()
                    .map(Map.Entry::getValue)
                    .filter(value -> !value.isEmpty())
                    .map(String::trim);
        }
        return profile;
    }

    /**
     * Return profile explicitly made active for this environment. Profiles can be
     * activated by setting {@linkplain Environment#ACTIVE_PROFILE_SYSTEM_PROPERTY_NAME "spring.profiles.active"} as a system property
     * or as property {@linkplain Environment#ACTIVE_PROFILE_APPLICATION_PROPERTY_NAME
     * * "spring.profile" set in "application.properties" resource file}
     */
    @Override
    public String getActiveProfile() {
        return activeProfile;
    }

    /**
     * Return the property value associated with the given key,
     * or {@code null} if the key cannot be resolved.
     *
     * @param key the property name to resolve
     */
    @Override
    public String getProperty(String key) {
        return propertiesMap.get(key);
    }
}
