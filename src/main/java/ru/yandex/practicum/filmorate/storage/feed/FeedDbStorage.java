package ru.yandex.practicum.filmorate.storage.feed;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedDbStorage implements FeedStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getEventsByUserId(Long id) {
        String sqlQuery = "SELECT * FROM FEED WHERE USER_ID = ?";
        List<Event> events;
        try {
            events = jdbcTemplate.query(sqlQuery, this::makeRowToEvent, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Like not found");
        }
        return events;
    }

    @Override
    public Event addEvent(Event event) {
        String sqlQuery = "INSERT INTO FEED (creation_time, user_id, event_type, operation, entity_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"event_id"});
            statement.setLong(1, event.getTimestamp());
            statement.setLong(2, event.getUserId());
            statement.setString(3, event.getEventType().toString());
            statement.setString(4, event.getOperation().toString());
            statement.setLong(5, event.getEntityId());
            return statement;
        }, keyHolder);
        event.setEventId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return event;
    }

    private Event makeRowToEvent(ResultSet resultSet, int nowNum) throws SQLException {
        return Event.builder()
                .timestamp(resultSet.getLong("creation_time"))
                .userId(resultSet.getLong("user_id"))
                .eventType(EventType.valueOf(resultSet.getString("event_type")))
                .operation(Operation.valueOf(resultSet.getString("operation")))
                .eventId(resultSet.getLong("event_id"))
                .entityId(resultSet.getLong("entity_id"))
                .build();
    }
}
