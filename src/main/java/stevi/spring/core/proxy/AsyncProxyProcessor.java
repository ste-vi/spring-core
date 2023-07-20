package stevi.spring.core.proxy;

import stevi.spring.core.anotation.Async;
import stevi.spring.core.anotation.Configuration;
import stevi.spring.core.context.Application;
import stevi.spring.core.proxy.util.ProxyUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * Implementation of {@link ProxyProcessor} to support {@link Async} annotation.
 * Methods with the annotation will be executed in another thread.
 */
public class AsyncProxyProcessor implements ProxyProcessor {

    @Override
    public Object replaceWithProxy(Object realObject, Class<?> implClass) {
        if (!implClass.isAnnotationPresent(Configuration.class)) {
            Function<Object, InvocationHandler> invocationHandlerFunction = object -> createInvocationHandler(realObject);
            return ProxyUtils.createProxy(realObject, implClass, invocationHandlerFunction);
        }
        return realObject;
    }

    private InvocationHandler createInvocationHandler(Object object) {
        return (proxy, method, args) -> {
            if (method.isAnnotationPresent(Async.class)) {
                Callable<Object> task = prepareCallableTask(object, method, args);
                return doSubmit(method, task);
            } else {
                return method.invoke(object, args);
            }
        };
    }

    private Callable<Object> prepareCallableTask(Object object, Method method, Object[] args) {
        return () -> {
            try {
                Object result = method.invoke(object, args);
                if (result instanceof Future<?> future) {
                    return future.get();
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return null;
        };
    }

    private Future<Object> doSubmit(Method method, Callable<Object> task) {
        Class<?> returnType = method.getReturnType();
        if (CompletableFuture.class.isAssignableFrom(returnType) || Future.class.isAssignableFrom(returnType)) {
            return Application.applicationFixedExecutorService.submit(task);
        } else if (void.class == returnType) {
            Application.applicationFixedExecutorService.submit(task);
            return null;
        } else {
            throw new IllegalArgumentException("Invalid return type for async method (only Future and void supported): " + returnType);
        }
    }
}
