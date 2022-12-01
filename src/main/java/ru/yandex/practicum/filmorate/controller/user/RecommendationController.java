package ru.yandex.practicum.filmorate.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.user.RecommendationService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{id}/recommendations")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecommendationController {
    RecommendationService recommendationService;

    @GetMapping
    public List<Film> getUserRecommendations(@Positive @PathVariable("id") Long id) {
        return recommendationService.getUserRecommendations(id);
    }
}
