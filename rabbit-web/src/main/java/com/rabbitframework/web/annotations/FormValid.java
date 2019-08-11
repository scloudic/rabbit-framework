package com.rabbitframework.web.annotations;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FormValid {
    String type() default "ajax";

    String fieldFilter() default "";


}
