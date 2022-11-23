package ru.yandex.practicum.filmorate.controller.film;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film)  {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long filmId) {
        return filmService.findFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms (@RequestParam(defaultValue = "10") @Positive Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedDirectorFilms(@PathVariable("directorId") Long directorId,
                                                 @RequestParam String sortBy) {
        return filmService.getSortedDirectorFilms(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> getTopSortedSearchedFilms(@RequestParam String query,@RequestParam String by) {
        return filmService.getTopSortedSearchedFilms(query, by);
    }
}
