package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.event.Event;

import java.util.List;

public interface FeedStorage {
    List<Event> getEventsByUserId(Long id);
    Event addEvent(Event event);
}
