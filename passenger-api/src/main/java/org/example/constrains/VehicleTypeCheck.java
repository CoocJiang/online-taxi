package org.example.constrains;

import com.google.j2objc.annotations.RetainedWith;
import org.apache.commons.lang.Validate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Constraint(validatedBy = VehiecleTypeCheckValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface VehicleTypeCheck {
    /**
     * 车辆类型选项
     * @return
     */
    String [] vehiecleTypeValue() default {};
    /**
     * 默认提示语
     * @return
     */
    String message() default "车辆类型错误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
