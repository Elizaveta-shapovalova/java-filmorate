package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorDbTest {
    JdbcTemplate jdbcTemplate;
    DirectorDbStorage directorDbStorage;
    FilmDbStorage filmDbStorage;

    private Director getDirector() {
        return Director.builder().name("testName").build();
    }

    private Film getFilm() {
        return Film.builder().name("testName").releaseDate(Date.valueOf("1979-04-17").toLocalDate()).description("testDescription").duration(100).rate(4).mpa(Mpa.builder().id(1L).name("G").build()).genres(new LinkedHashSet<>()).directors(new LinkedHashSet<>()).build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FILM_DIRECTOR");
        jdbcTemplate.update("DELETE FROM DIRECTORS");
        jdbcTemplate.update("ALTER TABLE DIRECTORS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    void testFindAllDirectors() {
        Director director = directorDbStorage.create(getDirector());
        List<Director> directors = List.of(director);
        assertEquals(directors, directorDbStorage.getAll());
    }

    @Test
    void testFindDirectorById() {
        Director director = directorDbStorage.create(getDirector());
        assertEquals(director, directorDbStorage.getById(director.getId()));
    }

    @Test
    void testCreateDirector() {
        Director director = getDirector();
        Director savedDirector = directorDbStorage.create(director);
        director.setId(1L);
        assertEquals(director, savedDirector);
    }

    @Test
    void testUpdateDirector() {
        Director director = directorDbStorage.create(getDirector());
        director.setName("testUpdateName");
        assertEquals(director, directorDbStorage.update(director));
    }

    @Test
    void testDeleteDirectorById() {
        Director director = directorDbStorage.create(getDirector());
        assertEquals(director, directorDbStorage.getById(director.getId()));
        directorDbStorage.deleteById(director.getId());
        assertEquals(new ArrayList<>(), directorDbStorage.getAll());
    }

    @Test
    void testAddFilmsDirector() {
        Film film = filmDbStorage.create(getFilm());
        LinkedHashSet<Director> directors = new LinkedHashSet<>();
        Director director = directorDbStorage.create(getDirector());
        directors.add(director);
        assertDoesNotThrow(() -> directorDbStorage.addFilmDirectors(film.getId(), directors));
    }

    @Test
    void testDeleteFilmsDirector() {
        Film film = filmDbStorage.create(getFilm());
        LinkedHashSet<Director> directors = new LinkedHashSet<>();
        Director director = directorDbStorage.create(getDirector());
        directors.add(director);
        assertDoesNotThrow(() -> directorDbStorage.addFilmDirectors(film.getId(), directors));
        assertDoesNotThrow(() -> directorDbStorage.deleteFilmDirectors(film.getId()));
    }
}