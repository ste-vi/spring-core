package stevi.spring.core.beanpostprocessor;

import stevi.spring.core.context.ApplicationContext;

/**
 * Factory hook that allows for custom modification of new bean instances &mdash;
 * for example, checking for marker interfaces or wrapping beans with proxies.
 *
 * <p>Typically, post-processors that populate beans via marker interfaces
 * or the like will implement {@link #postProcessBeforeInitialization},
 * while post-processors that wrap beans with proxies will normally
 * implement {@link #postProcessAfterInitialization}.
 */
public interface BeanPostProcessor {

    /**
     * Apply this {@code BeanPostProcessor} to the given new bean instance <i>before</i> any bean
     * initialization callbacks (like custom init-method via {@link stevi.spring.core.anotations.PostConstruct}).
     */
    void postProcessBeforeInitialization(Object bean, ApplicationContext applicationContext);

    /**
     * Apply this {@code BeanPostProcessor} to the given new bean instance <i>after</i> any bean
     * initialization callbacks (like custom init-method via {@link stevi.spring.core.anotations.PostConstruct}).
     */
    void postProcessAfterInitialization(Object bean, ApplicationContext applicationContext);
}
