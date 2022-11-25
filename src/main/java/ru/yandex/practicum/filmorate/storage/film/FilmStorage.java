package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film findFilmById(Long filmId);

    List<Film> getTopFilms(Integer count, Optional<Integer> genreId, Optional<Integer> year);

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> getSortedDirectorFilms(Long directorId, String sortBy);
}
