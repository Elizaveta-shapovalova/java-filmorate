package ru.yandex.practicum.filmorate.model.film;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Like {
    @NotNull
    Long filmId;
    @NotNull
    Long userId;
}
