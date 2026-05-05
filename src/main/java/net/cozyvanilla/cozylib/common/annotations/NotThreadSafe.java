package net.cozyvanilla.cozylib.common.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface NotThreadSafe {
}