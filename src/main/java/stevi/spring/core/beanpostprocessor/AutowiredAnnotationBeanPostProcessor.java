package stevi.spring.core.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.core.anotation.Autowired;
import stevi.spring.core.anotation.Configuration;
import stevi.spring.core.anotation.Qualifier;
import stevi.spring.core.context.ApplicationContext;

import java.lang.reflect.Field;

/**
 * BeanPostProcessor for enabling field injection of beans into other beans.
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    /**
     * Checks for {@link Autowired} annotation of declared field of the given bean. <br>
     * Gets a field type bean from {@link ApplicationContext} and assign value to it.
     * <br>
     * if {@link Qualifier} annotation is present, injects concrete class by provided name.
     */
    @SneakyThrows
    @Override
    public void postProcessBeforeInitialization(Object bean, ApplicationContext applicationContext) {
        if (!bean.getClass().isAnnotationPresent(Configuration.class)) {
            for (var declaredField : bean.getClass().getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)) {
                    Class<?> type = declaredField.getType();

                    type = getQualifiedImplementationIfNeeded(applicationContext, declaredField, type);
                    Object autowiredBean = applicationContext.getBean(type);

                    declaredField.setAccessible(true);
                    declaredField.set(bean, autowiredBean);
                }
            }
        }
    }

    private Class<?> getQualifiedImplementationIfNeeded(ApplicationContext applicationContext, Field declaredField, Class<?> type) {
        if (type.isInterface() && declaredField.isAnnotationPresent(Qualifier.class)) {
            String beanName = declaredField.getAnnotation(Qualifier.class).beanName();
            type = applicationContext.getConfig().getImplementationByBeanName(type, beanName);
        }
        return type;
    }

    @Override
    public void postProcessAfterInitialization(Object bean, ApplicationContext applicationContext) {

    }
}
