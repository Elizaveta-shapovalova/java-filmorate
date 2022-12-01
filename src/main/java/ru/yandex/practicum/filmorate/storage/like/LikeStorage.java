package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.film.Like;

import java.util.List;

public interface LikeStorage {
    void addLike(Long userId, Long filmId);
    void deleteLike(Long userId, Long filmId);
    List<Like> getLikes(Long userId, Long filmId);
}
