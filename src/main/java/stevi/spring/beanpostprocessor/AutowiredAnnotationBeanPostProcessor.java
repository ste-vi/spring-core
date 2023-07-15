package stevi.spring.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.anotations.Autowired;
import stevi.spring.factory.ObjectFactory;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(Object object) {

    }

    @SneakyThrows
    @Override
    public void postProcessAfterInitialization(Object object) {
        for (var declaredField : object.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                declaredField.setAccessible(true);
                Object autowiredObject = ObjectFactory.getInstance().createObject(declaredField.getType());
                declaredField.set(object, autowiredObject);
            }
        }
    }
}
