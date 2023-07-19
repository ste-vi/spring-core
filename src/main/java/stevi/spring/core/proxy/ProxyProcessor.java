package stevi.spring.core.proxy;

/**
 * Factory hook that allows wrapping beans with proxy objects &mdash;
 * for example, adding support for AOP.
 */
public interface ProxyProcessor {

    /**
     * Replaces real object with proxy.
     *
     * @param realObject actual object to create proxy on. (might be another proxy)
     * @param implClass  actual class
     * @return proxy object (might return the same object, depends on the implementation)
     */
    Object replaceWithProxy(Object realObject, Class<?> implClass);
}
