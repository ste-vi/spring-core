package stevi.spring.core.aop;

public interface ProxyProcessor {

    Object replaceWithProxy(Object realObject, Class<?> implClass);
}
