package ru.yandex.practicum.filmorate.model.user;

import lombok.*;
import lombok.experimental.FieldDefaults;


import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Follow {
    @NotNull
    final Long userId;
    @NotNull
    final Long friendId;
}
