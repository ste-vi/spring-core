package stevi.spring.core.anotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that marks a method as a candidate for asynchronous execution.
 * Applicable for public methods. <br>
 * Type of method should void or return {@link java.util.concurrent.Future}, or else it would not work.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
}
