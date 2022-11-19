package ru.yandex.practicum.filmorate.model.film;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@FieldDefaults(level= AccessLevel.PRIVATE)
public class Mpa {
    @NotNull
    Long id;
    @NotBlank
    String name;
}
