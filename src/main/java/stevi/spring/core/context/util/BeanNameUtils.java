package stevi.spring.core.context.util;

/**
 * Util class for handling bean names
 */
public final class BeanNameUtils {

    public static <T> String getBeanNameFromClass(Class<T> aClass) {
        String classSimpleName = aClass.getSimpleName();
        return formatBeanName(classSimpleName);
    }

    public static String formatBeanName(String beanName) {
        return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
    }
}
