package io.github.lr.ip2country.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion para el uso de la politica de reintento.
 * 
 * @author lravanal
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RetryPolicy {

}
