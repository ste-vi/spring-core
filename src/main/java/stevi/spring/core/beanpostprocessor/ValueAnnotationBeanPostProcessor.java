package stevi.spring.core.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.core.anotations.Value;
import stevi.spring.core.context.ApplicationContext;
import stevi.spring.core.env.Environment;

public class ValueAnnotationBeanPostProcessor implements BeanPostProcessor {

    @SneakyThrows
    @Override
    public void postProcessBeforeInitialization(Object bean, ApplicationContext applicationContext) {
        Class<?> objectClass = bean.getClass();
        for (var declaredField : objectClass.getDeclaredFields()) {
            Value valueAnnotation = declaredField.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                Environment environment = applicationContext.getBean(Environment.class);
                String value = valueAnnotation.value().isEmpty() ? environment.getProperty(declaredField.getName()) : valueAnnotation.value();
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }

    @Override
    public void postProcessAfterInitialization(Object bean, ApplicationContext applicationContext) {

    }
}
