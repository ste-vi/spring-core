package stevi.spring.core.beanpostprocessor;

import stevi.spring.core.context.ApplicationContext;

public interface BeanPostProcessor {

    void postProcessBeforeInitialization(Object object, ApplicationContext applicationContext);

    void postProcessAfterInitialization(Object object, ApplicationContext applicationContext);
}
