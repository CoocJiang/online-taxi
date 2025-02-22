package org.example.constrains;





import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DateTimeRangeValidator implements ConstraintValidator<DateTimeRange, Object> {
    DateTimeRange dateTimeRange;

    @Override
    public void initialize(DateTimeRange constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object datetime, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime param = null;
        // 看传过来的时间是字符串还是日期类型
        if (datetime instanceof LocalDateTime){
            param = (LocalDateTime) datetime;
        }else if (datetime instanceof String){
            param  = LocalDateTime.parse((String)datetime, DateTimeFormatter.ofPattern(dateTimeRange.pattern()));
        }
        LocalDateTime now = LocalDateTime.now();

        if (param.isAfter(now)) {
            return true;
        }
        return false;
    }

}
