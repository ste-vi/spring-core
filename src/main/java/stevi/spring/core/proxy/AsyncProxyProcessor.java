package stevi.spring.core.proxy;

import stevi.spring.core.anotations.Async;
import stevi.spring.core.proxy.util.ProxyUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

/**
 * Implementation of {@link ProxyProcessor} to support {@link Async} annotation.
 * Methods with the annotation will be executed in another thread.
 */
public class AsyncProxyProcessor implements ProxyProcessor {

    @Override
    public Object replaceWithProxy(Object realObject, Class<?> implClass) {
        for (var declaredMethod : implClass.getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Async.class)) {
                Function<Object, InvocationHandler> invocationHandlerFunction = Object -> createInvocationHandler(realObject);
                return ProxyUtils.createProxy(realObject, implClass, invocationHandlerFunction, declaredMethod.getName());
            } else {
                return realObject;
            }
        }
        return realObject;
    }


    private InvocationHandler createInvocationHandler(Object object) {
        return (proxy, method, args) -> {

            Runnable task = () -> {
                try {
                    System.out.println("BEFORE");
                    method.invoke(object, args);
                    System.out.println("AFTER");
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            };

            Thread thread = new Thread(task);
            thread.start();

            // return Application.applicationFixedExecutorService.submit(task);
            return null;
        };
    }
}
