package ru.yandex.practicum.filmorate.service.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorService {
    DirectorStorage directorStorage;

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getById(Long id) {
        return directorStorage.getById(id);
    }

    public Director create(Director director) {
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        return directorStorage.update(director);
    }

    public void deleteById(Long id) {
        directorStorage.deleteById(id);
    }

    public void addFilmDirectors(Long filmId, LinkedHashSet<Director> directors) {
        directorStorage.addFilmDirectors(filmId, directors);
    }

    public void deleteFilmDirectors(Long filmId) {
        directorStorage.deleteFilmDirectors(filmId);
    }

    public void loadDirectors(List<Film> films) {
        directorStorage.loadDirectors(films);
    }
}
