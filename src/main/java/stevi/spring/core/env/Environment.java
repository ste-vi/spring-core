package stevi.spring.core.env;

/**
 * Interface representing the environment in which the current application is running.<br>
 * Models two key aspects of the application environment: profile and properties.
 */
public interface Environment {

    String ACTIVE_PROFILE_APPLICATION_PROPERTY_NAME = "spring.profile.active";

    String ACTIVE_PROFILE_SYSTEM_PROPERTY_NAME = "spring.profile";

    String APPLICATION_PROPERTIES_FILE = "application.properties";

    String CUSTOM_ENV_APPLICATION_PROPERTIES_FILE = "application-%s.properties";

    /**
     * Return profile explicitly made active for this environment. Profiles can be
     * activated by setting {@linkplain Environment#ACTIVE_PROFILE_SYSTEM_PROPERTY_NAME "spring.profiles.active"} as a system property
     * or as property {@linkplain Environment#ACTIVE_PROFILE_APPLICATION_PROPERTY_NAME
     * * "spring.profile" set in "application.properties" resource file}
     */
    String getActiveProfile();

    /**
     * Return the property value associated with the given key,
     * or {@code null} if the key cannot be resolved.
     *
     * @param key the property name to resolve
     */
    String getProperty(String key);
}
