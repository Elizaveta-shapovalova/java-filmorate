package ru.yandex.practicum.filmorate.model.film;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Director {
    @NotNull(groups = {Marker.OnUpdate.class})
    Long id;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, message = "Имя режиссёра не может быть пустым.")
    String name;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        return values;
    }
}
