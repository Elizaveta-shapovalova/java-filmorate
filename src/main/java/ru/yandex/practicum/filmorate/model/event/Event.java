package ru.yandex.practicum.filmorate.model.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import javax.validation.constraints.NotNull;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @NotNull
    Long timestamp;
    @NotNull
    Long userId;
    @NotNull
    EventType eventType;
    @NotNull
    Operation operation;
    Long eventId;
    @NotNull
    Long entityId;

    public static Event createEvent(Long userId, EventType eventType, Operation operation, Long entityId) {
        return Event.builder()
                .timestamp(System.currentTimeMillis())
                .userId(userId)
                .eventType(eventType)
                .operation(operation)
                .entityId(entityId)
                .build();
    }
}
