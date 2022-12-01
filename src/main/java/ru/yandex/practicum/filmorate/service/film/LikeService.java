package ru.yandex.practicum.filmorate.service.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LikeService {
    LikeStorage likeStorage;
    UserService userService;
    FilmService filmService;
    FeedService feedService;

    public void addLike(Long userId, Long filmId) {
        filmService.getById(filmId);
        userService.getById(userId);
        likeStorage.addLike(userId, filmId);
        feedService.addEvent(Event.createEvent(userId, EventType.LIKE, Operation.ADD, filmId));
    }

    public void deleteLike(Long userId, Long filmId) {
        likeStorage.deleteLike(userId, filmId);
        feedService.addEvent(Event.createEvent(userId, EventType.LIKE, Operation.REMOVE, filmId));
    }
}
