package stevi.spring.core.context;

import stevi.spring.core.context.util.BeanNameUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class represents bean cache.
 * Hold all beans in a map and provides access to it.
 */
public class BeanCache {

    private final Map<String, Object> cache;

    public BeanCache() {
        cache = new ConcurrentHashMap<>();
    }

    public boolean contains(String beanName) {
        return cache.containsKey(beanName);
    }

    public boolean contains(Class<?> beanClass) {
        String beanName = BeanNameUtils.getBeanNameFromClass(beanClass);
        return cache.containsKey(beanName);
    }

    public Object get(String beanName) {
        return cache.get(beanName);
    }

    public Object get(Class<?> beanClass) {
        String beanName = BeanNameUtils.getBeanNameFromClass(beanClass);
        return cache.get(beanName);
    }

    /**
     * Puts a bean into cache. Key - is bean class name starts from lower case.
     */
    public void put(Object bean) {
        String beanName = BeanNameUtils.getBeanNameFromClass(bean.getClass());
        cache.put(beanName, bean);
    }

    public void put(String beanName, Object bean) {
        cache.put(beanName, bean);
    }
}
