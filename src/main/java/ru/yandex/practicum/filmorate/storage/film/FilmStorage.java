package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.enums.DirectorSortBy;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getAll();
    Film getById(Long id);
    Film create(Film film);
    Film update(Film film);
    void deleteById(Long filmId);
    List<Film> getTop(Integer count, Optional<Integer> genreId, Optional<Integer> year);
    List<Film> getCommon(Long userId, Long friendId);
    List<Film> getDirectorFilmsBy(Long directorId, DirectorSortBy filmSortBy);
    List<Film> getSearchedTopFilmsBy(String query, boolean searchByFilmName, boolean searchByDirectorName);
}
