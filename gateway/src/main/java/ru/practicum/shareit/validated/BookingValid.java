package ru.practicum.shareit.validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE_USE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = CheckBookingValidate.class)
public @interface BookingValid {
    String message() default "Дата старта не может быть раньше даты окончания";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
