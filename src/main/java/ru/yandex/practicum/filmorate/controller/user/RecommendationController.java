package ru.yandex.practicum.filmorate.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.RecommendationService;

import java.util.List;

@RestController
@RequestMapping("/users/{id}/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping
    public List<Film> getRecommendations(@PathVariable("id") Long userId) {
        return recommendationService.getRecommendations(userId);
    }
}
