package stevi.spring.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.anotations.Autowired;
import stevi.spring.context.ApplicationContext;
import stevi.spring.factory.ObjectFactory;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(Object object) {

    }

    @SneakyThrows
    @Override
    public void postProcessAfterInitialization(Object object, ApplicationContext applicationContext) {
        for (var declaredField : object.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                Object autowiredObject = applicationContext.getObect(declaredField.getType());
                declaredField.setAccessible(true);
                declaredField.set(object, autowiredObject);
            }
        }
    }
}
