package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.feed.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedDBTest {
    JdbcTemplate jdbcTemplate;
    FeedDbStorage feedDbStorage;
    UserDbStorage userDbStorage;

    User makeUser(){
        return User.builder()
                .email("test@yandex.ru")
                .login("test")
                .name("test")
                .birthday(LocalDate.of(2022, 1, 1))
                .build();
    }

    @BeforeEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FEED");
        jdbcTemplate.update("ALTER TABLE FEED ALTER COLUMN EVENT_ID RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void testSaveEvent(){
        final User user = userDbStorage.create(makeUser());
        final Event eventCreate = Event.createEvent(user.getId(), EventType.LIKE, Operation.ADD, 3L);
        final Event eventSave = feedDbStorage.addEvent(eventCreate);
        final Map<Long, Event> eventWithId = Stream.of(eventSave).collect(Collectors.toMap(Event::getEventId, identity()));

        assertEquals(eventCreate, eventWithId.get(eventSave.getEventId()), "Created Event and saved Event don't match.");
        assertEquals(1L, eventSave.getEventId(), "Event id don't match.");
    }

    @Test
    void testFindEventsByUserId(){
        final User user = userDbStorage.create(makeUser());

        assertTrue(feedDbStorage.getEventsByUserId(user.getId()).isEmpty(), "List Event don't empty.");

        feedDbStorage.addEvent(Event.createEvent(user.getId(), EventType.LIKE, Operation.ADD, 3L));
        final List<Event> events = feedDbStorage.getEventsByUserId(user.getId());

        assertNotNull(events, "Event don't save.");
        assertEquals(1, events.size(), "Size of list events don't match.");

        feedDbStorage.addEvent(Event.createEvent(user.getId(), EventType.LIKE, Operation.ADD, 5L));
        final List<Event> eventsForTwo = feedDbStorage.getEventsByUserId(user.getId());

        assertNotNull(eventsForTwo, "Event don't save.");
        assertEquals(2, eventsForTwo.size(), "Size of list events don't match.");
    }
}
