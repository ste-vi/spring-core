package stevi.spring.beanpostprocessor;

public interface BeanPostProcessor {

    void postProcessBeforeInitialization(Object object);

    void postProcessAfterInitialization(Object object);
}
