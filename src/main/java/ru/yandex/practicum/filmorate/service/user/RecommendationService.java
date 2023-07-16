package ru.yandex.practicum.filmorate.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.film.DirectorService;
import ru.yandex.practicum.filmorate.service.film.GenreService;
import ru.yandex.practicum.filmorate.storage.recommendation.RecommendationStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecommendationService {
    RecommendationStorage recommendationStorage;
    UserService userService;
    GenreService genreService;
    DirectorService directorService;

    public List<Film> getUserRecommendations(Long id) {
        userService.getById(id);
        List<Film> films = recommendationStorage.getUserRecommendations(id);
        genreService.loadGenres(films);
        directorService.loadDirectors(films);
        return films;
    }
}
