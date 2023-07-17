package stevi.spring.aop;

public interface ProxyProcessor {

    Object replaceWithProxy(Object realObject, Class<?> implClass);
}
