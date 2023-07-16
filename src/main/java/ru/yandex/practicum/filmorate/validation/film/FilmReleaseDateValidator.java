package ru.yandex.practicum.filmorate.validation.film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDateConstraint, LocalDate> {
    public static final LocalDate DATE = LocalDate.of(1895, 12, 27);

    @Override
    public void initialize(FilmReleaseDateConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return releaseDate.isAfter(DATE);
    }
}
