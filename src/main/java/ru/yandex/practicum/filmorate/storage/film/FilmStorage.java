package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();
    Film create(Film film);
    Film update(Film film);
    Film findFilmById (Long filmId);
    List<Film> getTopFilms(Integer count);
    List<Film> getTopFilmsWithoutLimit();
    List<Film> getDirectorFilmsSortedByYear(Long directorId);
    List<Film> getDirectorFilmsSortedByLikes(Long directorId);
}
