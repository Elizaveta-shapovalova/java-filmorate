package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.storage.mark.MarkStorage;

@Service
@RequiredArgsConstructor
public class MarkService {
    private final MarkStorage markStorage;
    private final FeedService feedService;

    public void addMark(Long filmId, Long userId, Integer mark) {
        markStorage.add(filmId, userId, mark);
        feedService.addEvent(Event.createEvent(userId, EventType.LIKE, Operation.ADD, filmId));
    }

    public void deleteMark(Long filmId, Long userId) {
        markStorage.remove(filmId, userId);
        feedService.addEvent(Event.createEvent(userId, EventType.LIKE, Operation.REMOVE, filmId));
    }
}
