package ru.practicum.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = EventDateValidator.class)
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface EventDateAfterHours {
    String message() default "Событие не может быть раньше, чем определенное время от текущего момента.";
    int hours();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
