package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.recommendation.RecommendationDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecommendationDbTest {
    RecommendationDbStorage recommendationDbStorage;
    UserDbStorage userDbStorage;
    FilmDbStorage filmDbStorage;
    LikeDbStorage likeDbStorage;

    @Test
    void testGetRecommendations() {
        final Film film = filmDbStorage.createFilm(Film.builder()
                .name("testName")
                .releaseDate(Date.valueOf("1979-04-17").toLocalDate())
                .description("testDescription")
                .duration(100)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(new LinkedHashSet<>())
                .directors(new LinkedHashSet<>()).build());
        final Film secondFilm = filmDbStorage.createFilm(Film.builder()
                .name("secondTestName")
                .releaseDate(Date.valueOf("1979-04-17").toLocalDate())
                .description("secondTestDescription")
                .duration(100)
                .rate(4)
                .mpa(Mpa.builder().id(1L).name("G").build())
                .genres(new LinkedHashSet<>())
                .directors(new LinkedHashSet<>()).build());
        final User user = userDbStorage.createUser(User.builder()
                .email("test@yandex.ru")
                .login("testLogin")
                .birthday(LocalDate.of(2022, 1, 1))
                .build());
        final User secondUser = userDbStorage.createUser(User.builder()
                .email("secondTest@yandex.ru")
                .login("secondTestLogin")
                .birthday(LocalDate.of(2022, 1, 1))
                .build());
        likeDbStorage.addLike(user.getId(), film.getId());
        likeDbStorage.addLike(user.getId(), secondFilm.getId());
        likeDbStorage.addLike(secondUser.getId(), film.getId());
        List<Film> films = recommendationDbStorage.getRecommendations(secondUser.getId());

        assertNotNull(films, "List recommendation is empty.");
        assertEquals(secondFilm, films.get(0), "Recommendation film don't match.");

        likeDbStorage.deleteLike(user.getId(), secondFilm.getId());
        List<Film> emptyFilms = recommendationDbStorage.getRecommendations(secondUser.getId());

        assertTrue(emptyFilms.isEmpty(), "List recommendation isn't empty.");
    }
}

