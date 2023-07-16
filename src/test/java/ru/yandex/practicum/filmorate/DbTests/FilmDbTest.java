package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FilmDbTest {
    JdbcTemplate jdbcTemplate;
    FilmDbStorage filmDbStorage;
    UserDbStorage userDbStorage;
    LikeDbStorage likeDbStorage;

    private Film getFilm() {
        return Film.builder().name("testName").releaseDate(Date.valueOf("1979-04-17").toLocalDate()).description("testDescription").duration(100).rate(4).mpa(Mpa.builder().id(1L).name("G").build()).genres(new LinkedHashSet<>()).directors(new LinkedHashSet<>()).build();
    }

    private User getUser() {
        return User.builder().email("test@mail.ru").login("testLogin").name("testName").birthday(Date.valueOf("1946-08-20").toLocalDate()).build();
    }

    private Genre getGenre() {
        return Genre.builder().id(2L).name("Драма").build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM LIKES");
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM MOVIE");
        jdbcTemplate.update("DELETE FROM DIRECTORS");
        jdbcTemplate.update("ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE MOVIE ALTER COLUMN ID RESTART WITH 1");

    }

    @Test
    void testSaveFilm() {
        Film film = getFilm();
        Film savedFilm = filmDbStorage.create(film);
        film.setId(1L);
        assertEquals(film, savedFilm);
    }

    @Test
    void testSaveFilmWithEmptyName() {
        Film film = getFilm();
        film.setName("");
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.create(film), "Имя не должно быть пустым.");
    }

    @Test
    void testSaveFilmWithLongDescription() {
        Film film = getFilm();
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " + "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " + "который за время «своего отсутствия», стал кандидатом Коломбани.");
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.create(film), "Описание не должно иметь более 200 символов.");
    }

    @Test
    void testSaveFilmWithOldReleaseDate() {
        Film film = getFilm();
        film.setReleaseDate(Date.valueOf("1895-12-27").toLocalDate());
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.create(film), "Дата выхода не может быть раньше 28.12.1895 г.");
    }

    @Test
    void testSaveFilmWithNotPositiveDuration() {
        Film film = getFilm();
        film.setDuration(0);
        assertThrows(DataIntegrityViolationException.class, () -> filmDbStorage.create(film), "Продолжительность должна быть положительной.");
    }

    @Test
    void testSaveFilmWithNullMpa() {
        Film film = getFilm();
        film.setMpa(null);
        assertThrows(NullPointerException.class, () -> filmDbStorage.create(film), "MPA не может быть 'null'.");
    }

    @Test
    void deleteFilm() {
        Film film = getFilm();
        filmDbStorage.create(film);
        filmDbStorage.deleteById(1L);
        assertEquals(0, filmDbStorage.getAll().size());
    }

    @Test
    void testUpdateFilm() {
        Film film = filmDbStorage.create(getFilm());
        film.setName("testUpdateName");
        assertEquals(film, filmDbStorage.update(film));
    }

    @Test
    void testFindAllFilms() {
        Film film = filmDbStorage.create(getFilm());
        List<Film> films = List.of(film);
        assertEquals(films, filmDbStorage.getAll());
    }

    @Test
    void testFindFilmById() {
        Film film = filmDbStorage.create(getFilm());
        assertEquals(film, filmDbStorage.getById(1L));
    }

    @Test
    void testFindUnknownFilm() {
        assertThrows(ObjectNotFoundException.class, () -> filmDbStorage.getById(9999L), "Фильм с id " + 9999 + " не найден.");
    }

    @Test
    void testGetEmptyTopFilms() {
        assertEquals(new ArrayList<>(), filmDbStorage.getTop(10, Optional.empty(), Optional.empty()));
    }

    @Test
    void testDeleteLikesAfterDeletingFilm() {
        filmDbStorage.create(getFilm());
        userDbStorage.create(getUser());
        likeDbStorage.addLike(1L, 1L);
        assertEquals(1, likeDbStorage.getLikes(1L, 1L).size());
        filmDbStorage.deleteById(1L);
        assertEquals(0, likeDbStorage.getLikes(1L, 1L).size());
    }

    @Test
    void testFindTwoPopularFilms() {
        Film film = filmDbStorage.create(getFilm());
        Film film2 = filmDbStorage.create(getFilm());
        assertEquals(List.of(film, film2), filmDbStorage.getTop(10, Optional.empty(), Optional.empty()));
    }

    @Test
    void testUpdateFilmWithGenre() {
        Film film = filmDbStorage.create(getFilm());
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(getGenre());
        film.setGenres(genres);
        assertEquals(film, filmDbStorage.update(film));
    }

    @Test
    void testUpdateFilmWithRepeatedGenres() {
        Film film = filmDbStorage.create(getFilm());
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(getGenre());
        genres.add(Genre.builder().id(1L).name("Комедия").build());
        genres.add(Genre.builder().id(2L).name("Драма").build());
        genres.add(Genre.builder().id(1L).name("Комедия").build());
        film.setGenres(genres);
        Film updatedFilm = filmDbStorage.update(film);
        assertEquals(film, updatedFilm);
    }
}
