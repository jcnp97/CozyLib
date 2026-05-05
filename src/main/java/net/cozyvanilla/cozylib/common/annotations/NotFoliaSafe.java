package net.cozyvanilla.cozylib.common.annotations;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface NotFoliaSafe {
    String value() default "This method is not safe to call from async or wrong thread.";
}