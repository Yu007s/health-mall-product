package com.drstrong.health.product.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ValidUsageValidator.class})
public @interface ValidUsage {
    String message() default "Invalid usage";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
