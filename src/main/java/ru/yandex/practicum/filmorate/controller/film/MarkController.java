package ru.yandex.practicum.filmorate.controller.film;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.film.MarkService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping("films/{id}/mark/{userId}")
@RequiredArgsConstructor
public class MarkController {
    private final MarkService markService;

    @PutMapping
    public void addMark(@PathVariable("id") Long filmId,
                        @PathVariable("userId") Long userId,
                        @RequestParam(name = "mark") @Min(1) @Max(10) Integer mark) {
        markService.addMark(filmId, userId, mark);
    }

    @DeleteMapping
    public void removeMark(@PathVariable("id") Long filmId,
                           @PathVariable("userId") Long userId) {
        markService.deleteMark(filmId, userId);
    }
}
