package ru.yandex.practicum.filmorate.validation.film;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FilmReleaseDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FilmReleaseDateConstraint {
    String message() default "December 28, 1895 is considered the birthday of cinema.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
