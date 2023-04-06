package ru.practicum.shareit.validated;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.exception.ValidException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckBookingValidate implements ConstraintValidator<BookingValid, BookingDtoRequest> {
    @Override
    public void initialize(BookingValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDtoRequest booking, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start.isAfter(end) || start.equals(end)) {
            throw new ValidException("Дата уканана неверно");
        }
        return true;
    }
}
