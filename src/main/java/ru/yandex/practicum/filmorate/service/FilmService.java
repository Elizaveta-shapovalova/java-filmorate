package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.SearchType;
import ru.yandex.practicum.filmorate.model.film.SortType;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate FILM_BIRTH = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final GenreService genreService;
    private final DirectorService directorService;

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public Film create(Film film) {
        validator(film);
        Film filmWithId = filmStorage.create(film);
        if (film.getGenres() != null) {
            genreService.addFilmsGenre(filmWithId.getId(), film.getGenres());
        }
        if (film.getDirectors() != null) {
            directorService.addFilmsDirector(filmWithId.getId(), film.getDirectors());
        }
        return filmWithId;
    }

    public Film update(Film film) {
        validator(film);
        filmStorage.findFilmById(film.getId());
        if (film.getGenres() != null) {
            genreService.deleteFilmsGenre(film.getId());
            genreService.addFilmsGenre(film.getId(), film.getGenres());
        } else {
            genreService.deleteFilmsGenre(film.getId());
        }
        if (film.getDirectors() != null) {
            directorService.deleteFilmsDirector(film.getId());
            directorService.addFilmsDirector(film.getId(), film.getDirectors());
        } else {
            directorService.deleteFilmsDirector(film.getId());
        }
        return filmStorage.update(film);
    }

    public Film findFilmById(Long filmId) {
        Film film = filmStorage.findFilmById(filmId);
        genreService.loadGenres(List.of(film));
        directorService.loadDirectors(List.of(film));
        return film;
    }

    public List<Film> getTopFilms(Integer count, Optional<Integer> genreId, Optional<Integer> year) {
        List<Film> films = filmStorage.getTopFilms(count, genreId, year);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    private void validator(Film film) {
        if (film.getReleaseDate().isBefore(FILM_BIRTH)) {
            throw new ValidationException("December 28, 1895 is considered the birthday of cinema.");
        }
    }

    public List<Film> getSortedDirectorFilms(Long directorId, String sortBy) {
        directorService.findDirectorById(directorId);
        SortType sortType = SortType.valueOf(sortBy.toUpperCase());
        List<Film> films;
        if (sortType == SortType.YEAR) {
            films = filmStorage.getDirectorFilmsSortedByYear(directorId);
        } else if (sortType == SortType.LIKES) {
            films = filmStorage.getDirectorFilmsSortedByLikes(directorId);
        } else {
            throw new IllegalArgumentException("Некорректный тип сортировки");
        }
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }

    public List<Film> getTopSortedSearchedFilms(String query, String by) {
        List<SearchType> searchType = Arrays.stream(by.toUpperCase().split(",")).map(SearchType::valueOf).collect(Collectors.toList());
        List<Film> films = filmStorage.getTopFilmsWithoutLimit();
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        if (searchType.size() == 1 && searchType.get(0) == SearchType.DIRECTOR) {
            films = films.stream()
                    .filter(film -> film.getDirectors()
                            .stream()
                            .anyMatch(director -> director.getName().toLowerCase().contains(query.toLowerCase())))
                    .collect(Collectors.toList());
        } else if (searchType.size() == 1 && searchType.get(0) == SearchType.TITLE) {
            films = films.stream()
                    .filter(film -> film.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        } else if (searchType.size() == 2 && searchType.get(0) != searchType.get(1)) {
            films = films.stream()
                    .filter(film -> film.getDirectors()
                            .stream()
                            .anyMatch(director -> director.getName().toLowerCase().contains(query.toLowerCase()))
                            || film.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Некорректный тип поиска");
        }
        return films;
    }
}


