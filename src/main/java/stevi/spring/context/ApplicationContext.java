package stevi.spring.context;


import stevi.spring.config.Config;

public interface ApplicationContext {

    void postInit();

    <T> T getBean(Class<T> aClass);

    <T> T getBeanByName(String beanName);

    Config getConfig();

}
