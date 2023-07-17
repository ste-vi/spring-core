package stevi.spring.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.anotations.Autowired;
import stevi.spring.anotations.Qualifier;
import stevi.spring.context.ApplicationContext;

import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    @SneakyThrows
    @Override
    public void postProcessBeforeInitialization(Object object, ApplicationContext applicationContext) {
        for (var declaredField : object.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Autowired.class)) {
                Class<?> type = declaredField.getType();

                type = getQualifiedImplementationIfNeeded(applicationContext, declaredField, type);

                Object autowiredObject = applicationContext.getBean(type);
                declaredField.setAccessible(true);
                declaredField.set(object, autowiredObject);
            }
        }
    }

    @Override
    public void postProcessAfterInitialization(Object object, ApplicationContext applicationContext) {

    }

    private Class<?> getQualifiedImplementationIfNeeded(ApplicationContext applicationContext, Field declaredField, Class<?> type) {
        if (type.isInterface() && declaredField.isAnnotationPresent(Qualifier.class)) {
            String beanName = declaredField.getAnnotation(Qualifier.class).beanName();
            type = applicationContext.getConfig().getImplementationByBeanName(type, beanName);
        }
        return type;
    }
}
