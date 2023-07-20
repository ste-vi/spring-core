package stevi.spring.core.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>PostConstruct</code> annotation is used on a method that
 * needs to be executed after dependency injection is done to perform any initialization.
 * Serves as second constructor.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostConstruct {
}
