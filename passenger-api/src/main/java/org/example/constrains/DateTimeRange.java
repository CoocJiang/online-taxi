package org.example.constrains;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = VehiecleTypeCheckValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface DateTimeRange {

    String pattern() default "yyyy-MM-dd HH:mm:ss";

    String judge() default "isAfter";

    String message() default "日期错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
