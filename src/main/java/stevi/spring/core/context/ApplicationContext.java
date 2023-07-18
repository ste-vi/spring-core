package stevi.spring.core.context;


import stevi.spring.core.config.Config;

public interface ApplicationContext {

    void postInit();

    <T> T getBean(Class<T> aClass);

    <T> T getBeanByName(String beanName);

    Config getConfig();

}
