package org.example.constrains;





import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class VehiecleTypeCheckValidator implements ConstraintValidator<VehicleTypeCheck, String> {
    private List<String> vehiecleTypeValue = null;

    @Override
    public void initialize(VehicleTypeCheck constraintAnnotation) {
       vehiecleTypeValue =  Arrays.asList(constraintAnnotation.vehiecleTypeValue());
       ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (vehiecleTypeValue.contains(s)){
            return true;
        }else {
            return false;
        }
    }
}
