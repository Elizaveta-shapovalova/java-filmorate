package ru.yandex.practicum.filmorate.controller.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.film.enums.DirectorSortBy;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;


@Validated
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmController {
    FilmService filmService;

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getById(@Positive @PathVariable("id") Long id) {
        return filmService.getById(id);
    }

    @PostMapping
    public Film create(@Validated(Marker.OnCreate.class) @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Validated(Marker.OnUpdate.class) @RequestBody Film film) {
        return filmService.update(film);
    }

    @DeleteMapping("{filmId}")
    public void deleteById(@Positive @PathVariable("filmId") Long filmId) {
        filmService.deleteById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getTop(@Positive @RequestParam(defaultValue = "10") Integer count,
                             @RequestParam(required = false) Optional<Integer> genreId,
                             @RequestParam(required = false) Optional<Integer> year) {
        return filmService.getTop(count, genreId, year);
    }

    @GetMapping("/common")
    public List<Film> getCommon(@Positive @RequestParam(value = "userId") Long userId,
                                @Positive @RequestParam(value = "friendId") Long friendId) {
        return filmService.getCommon(userId, friendId);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getDirectorFilmsBy(@Positive @PathVariable("directorId") Long directorId,
                                         @RequestParam DirectorSortBy sortBy) {
        return filmService.getDirectorFilmsBy(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> getSearchedTopFilmsBy(@RequestParam(value = "query") String query,
                                            @RequestParam(value = "by") String by) {
        return filmService.getSearchedTopFilmsBy(query, by);
    }
}
