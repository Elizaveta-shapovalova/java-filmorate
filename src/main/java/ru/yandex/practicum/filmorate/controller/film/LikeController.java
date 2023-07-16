package ru.yandex.practicum.filmorate.controller.film;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.film.LikeService;

@RestController
@RequestMapping("/films/{id}/like/{userId}")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LikeController {
    LikeService likeService;

    @PutMapping
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        likeService.addLike(userId, filmId);
    }

    @DeleteMapping
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId) {
        likeService.deleteLike(userId, filmId);
    }
}
