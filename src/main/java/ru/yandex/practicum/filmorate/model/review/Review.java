package ru.yandex.practicum.filmorate.model.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @NotNull(groups = Marker.OnUpdate.class)
    Long reviewId;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    String content;
    @JsonProperty("isPositive")
    @NotNull(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    Boolean positive;
    @NotNull(groups = Marker.OnCreate.class)
    Long filmId;
    @NotNull(groups = Marker.OnCreate.class)
    Long userId;
    Long useful;

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("content", content);
        values.put("is_positive", positive);
        values.put("film_id", filmId);
        values.put("user_id", userId);
        return values;
    }
}
