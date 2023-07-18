package stevi.spring.core.aop;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import stevi.spring.core.anotations.Async;
import stevi.spring.core.context.Application;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class AsyncProxyProcessor implements ProxyProcessor {

    @Override
    public Object replaceWithProxy(Object realObject, Class<?> implClass) {
        for (var declaredMethod : implClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Async.class)) {
                return createProxy(realObject, implClass, declaredMethod.getName());
            } else {
                return realObject;
            }
        }
        return realObject;
    }

    @SneakyThrows
    <T> T createProxy(Object targetObject, Class<T> proxyInterface, String methodName) {
        return new ByteBuddy()
                .subclass(proxyInterface)
                .method(ElementMatchers.named(methodName))
                .intercept(InvocationHandlerAdapter.of(createInvocationHandler(targetObject)))
                .make()
                .load(proxyInterface.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    private InvocationHandler createInvocationHandler(Object object) {
        return (proxy, method, args) -> {

            Callable<Object> task = () -> {
                try {
                    return method.invoke(object, args);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            return Application.applicationFixedExecutorService.submit(task);
        };
    }
}
