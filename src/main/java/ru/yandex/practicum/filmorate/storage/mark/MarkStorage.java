package ru.yandex.practicum.filmorate.storage.mark;

public interface MarkStorage {
    void add(Long filmId, Long userId, Integer mark);
    void remove(Long filmId, Long userId);
}
