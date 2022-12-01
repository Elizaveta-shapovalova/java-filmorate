package ru.yandex.practicum.filmorate.service.film;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreService {
    GenreStorage genreStorage;

    public List<Genre> getAll() {
        return genreStorage.getAll();
    }

    public Genre getById(Long genreId) {
        return genreStorage.getById(genreId);
    }

    public void addGenresToFilm(Long filmId, LinkedHashSet<Genre> genres) {
        genreStorage.addGenresToFilm(filmId, genres);
    }

    public void deleteFilmGenres(Long filmId) {
        genreStorage.deleteFilmGenres(filmId);
    }

    public void loadGenres(List<Film> films) {
        genreStorage.loadGenres(films);
    }
}
