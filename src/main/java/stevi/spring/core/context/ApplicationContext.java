package stevi.spring.core.context;

import stevi.spring.core.config.Config;

/**
 * Central interface to provide configuration for an application.
 * Holds all created beans.
 */
public interface ApplicationContext {

    /**
     * Used for some additional configuration of the application.
     */
    void postInit();

    /**
     * Fetches bean from cache.
     * Created new bean and sets into cache of not found.
     *
     * @param aClass class to get bean based on
     */
    <T> T getBean(Class<T> aClass);

    /**
     * Fetches bean from cache by a bean name.
     * Throws exception if nothing is found;
     */
    <T> T getBeanByName(String beanName);

    /**
     * Gets {@link Config}
     */
    Config getConfig();

}
