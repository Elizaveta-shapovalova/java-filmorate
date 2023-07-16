package ru.yandex.practicum.filmorate.service.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.enums.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.enums.SearchSortBy;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmService {
    FilmStorage filmStorage;
    GenreService genreService;
    DirectorService directorService;

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public Film getById(Long id) {
        Film film = filmStorage.getById(id);
        genreService.loadGenres(List.of(film));
        directorService.loadDirectors(List.of(film));
        return film;
    }

    public Film create(Film film) {
        Film filmWithId = filmStorage.create(film);
        if (film.getGenres() != null) {
            genreService.addGenresToFilm(filmWithId.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmDirectors(filmWithId.getId(), film.getDirectors());
        }
        return filmWithId;
    }

    public Film update(Film film) {
        genreService.deleteFilmGenres(film.getId());
        directorService.deleteFilmDirectors(film.getId());
        if (film.getGenres() != null) {
            genreService.addGenresToFilm(film.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmDirectors(film.getId(), film.getDirectors());
        }
        return filmStorage.update(film);
    }

    public void deleteById(Long filmId) {
        filmStorage.deleteById(filmId);
    }

    public List<Film> getTop(Integer count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> films = filmStorage.getTop(count, genreId, year);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getCommon(Long userId, Long friendId) {
        List<Film> films = filmStorage.getCommon(userId, friendId);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getDirectorFilmsBy(Long directorId, DirectorSortBy sortBy) {
        directorService.getById(directorId);
        List<Film> films = filmStorage.getDirectorFilmsBy(directorId, sortBy);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getSearchedTopFilmsBy(String query, String by) {
        by = by.toLowerCase();
        boolean searchByFilmName = by.contains(SearchSortBy.title.name());
        boolean searchByDirectorName = by.contains(SearchSortBy.director.name());
        List<Film> films = filmStorage.getSearchedTopFilmsBy(query, searchByFilmName, searchByDirectorName);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }
}


