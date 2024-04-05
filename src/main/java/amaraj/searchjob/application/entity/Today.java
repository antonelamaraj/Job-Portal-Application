package amaraj.searchjob.application.entity;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.temporal.Temporal;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Today.TodayValidator.class)
public @interface Today {
    String message() default "date is not equal to today";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class TodayValidator implements ConstraintValidator<Today, Temporal>{

        @Override
        public void initialize(Today constraintAnnotation) {

        }

        @Override
        public boolean isValid(Temporal value, ConstraintValidatorContext context) {
            return value==null || LocalDate.from(value).isEqual(LocalDate.now());
        }
    }

}
