package ru.yandex.practicum.filmorate.storage.director;

import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.LinkedHashSet;
import java.util.List;

public interface DirectorStorage {
    List<Director> getAll();
    Director getById(Long id);
    Director create(Director director);
    Director update(Director director);
    void deleteById(Long id);
    void addFilmDirectors(Long filmId, LinkedHashSet<Director> directors);
    void deleteFilmDirectors(Long filmId);
    void loadDirectors(List<Film> films);
}
