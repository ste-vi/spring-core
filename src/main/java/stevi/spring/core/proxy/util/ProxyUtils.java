package stevi.spring.core.proxy.util;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationHandler;
import java.util.function.Function;

/**
 * Util class that helps to create a proxy class
 */
public final class ProxyUtils {

    /**
     * Creates proxy object based on extending from a target object.
     * Note: class must have default constructor.
     *
     * @param targetObject              actual object to proxy
     * @param implClass                 actual class to create proxy based on
     * @param invocationHandlerFunction function with ready written invocation handler and logic to be invoked
     * @return ready proxy object
     */
    @SneakyThrows
    public static <T> T createProxy(Object targetObject,
                                    Class<T> implClass,
                                    Function<Object, InvocationHandler> invocationHandlerFunction) {
        try {
            return new ByteBuddy()
                    .subclass(implClass)
                    .method(ElementMatchers.any())
                    .intercept(InvocationHandlerAdapter.of(invocationHandlerFunction.apply(targetObject)))
                    .make()
                    .load(implClass.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (NoSuchMethodException exception) {
            throw new RuntimeException("No default constructor is found for : %s".formatted(implClass));
        }

    }
}
