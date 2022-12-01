package ru.yandex.practicum.filmorate.model.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.Marker;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @NotNull(groups = Marker.OnUpdate.class)
    Long id;
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    String email;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Pattern(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
            regexp = "^\\S*$", message = "Your login must not contains space symbols.")
    String login;
    String name;
    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @PastOrPresent(groups = {Marker.OnCreate.class, Marker.OnUpdate.class},
            message = "Birthday must not be in future.")
    LocalDate birthday;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("email", email);
        values.put("login", login);
        values.put("birthday", birthday);
        return values;
    }

}
