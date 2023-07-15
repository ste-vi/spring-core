package stevi.spring.beanpostprocessor;

import stevi.spring.context.ApplicationContext;

public interface BeanPostProcessor {

    void postProcessBeforeInitialization(Object object);

    void postProcessAfterInitialization(Object object, ApplicationContext applicationContext);
}
