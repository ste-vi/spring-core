package stevi.spring.beanpostprocessor;

import lombok.SneakyThrows;
import stevi.spring.anotations.Value;
import stevi.spring.context.ApplicationContext;
import stevi.spring.util.PropertyUtils;

import java.util.Map;
import java.util.Optional;

public class ValueAnnotationBeanPostProcessor implements BeanPostProcessor {

    private final Map<String, String> propertiesMap;

    public ValueAnnotationBeanPostProcessor() {
        propertiesMap = PropertyUtils.fetchProperties("application.properties");

        Optional<String> activeProfile = propertiesMap
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().trim().equals("spring.profile.active"))
                .findFirst()
                .map(Map.Entry::getValue)
                .map(String::trim);

        if (activeProfile.isPresent()) {
            Map<String, String> defaultProfileProperties = PropertyUtils.fetchProperties("application-%s.properties".formatted(activeProfile.get()));
            propertiesMap.putAll(defaultProfileProperties);
        }

    }

    @SneakyThrows
    @Override
    public void postProcessBeforeInitialization(Object bean, ApplicationContext applicationContext) {
        Class<?> objectClass = bean.getClass();

        for (var declaredField : objectClass.getDeclaredFields()) {
            Value valueAnnotation = declaredField.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                String value = valueAnnotation.value().isEmpty() ? propertiesMap.get(declaredField.getName()) : valueAnnotation.value();
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            }
        }
    }

    @Override
    public void postProcessAfterInitialization(Object bean, ApplicationContext applicationContext) {

    }
}
