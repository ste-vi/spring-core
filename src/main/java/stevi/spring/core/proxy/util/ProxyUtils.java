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
     *
     * @param targetObject              actual object to proxy
     * @param implClass                 actual class to create proxy based on
     * @param invocationHandlerFunction function with ready written invocation handler and logic to be invoked
     * @param methodName                method name to substitute implementation of
     * @return ready proxy object
     */
    @SneakyThrows
    public static <T> T createProxy(Object targetObject,
                                    Class<T> implClass,
                                    Function<Object, InvocationHandler> invocationHandlerFunction,
                                    String methodName) {
        return new ByteBuddy()
                .subclass(implClass)
                .method(ElementMatchers.named(methodName))
                .intercept(InvocationHandlerAdapter.of(invocationHandlerFunction.apply(targetObject)))
                .make()
                .load(implClass.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}
