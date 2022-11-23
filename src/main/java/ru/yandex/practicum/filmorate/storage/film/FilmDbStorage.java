package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;


@Repository("filmStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM MOVIE AS m " +
                "INNER JOIN MPA ON MPA.id = m.mpa_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO movie(name, description, release_date, duration, rate,  mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE MOVIE SET " +
                "NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ? , DURATION = ?, RATE = ?, MPA_ID = ? " +
                "WHERE ID = ?";
        if (jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , film.getReleaseDate()
                , film.getDuration()
                , film.getRate()
                , film.getMpa().getId()
                , film.getId())
                == 0)
        {
            throw new FilmNotFoundException(String.format("Film with %d id not found", film.getId()));
        }
        return film;
    }

    @Override
    public Film findFilmById(Long filmId) {
        String sqlQuery = "SELECT * FROM MOVIE AS m " +
                "INNER JOIN MPA ON m.mpa_id = MPA.id " +
                "WHERE m.id = ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, filmId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with %d id not found", filmId)));

    }

    @Override
    public List<Film> getTopFilms(Integer count) {
        String sqlQuery = "SELECT * " +
                "FROM MOVIE AS m " +
                "INNER JOIN MPA ON MPA.id = m.mpa_id " +
                "LEFT JOIN likes AS l ON l.film_id = m.id " +
                "GROUP BY m.id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    @Override
    public List<Film> getTopFilmsWithoutLimit() {
        String sqlQuery = "SELECT * " +
                "FROM MOVIE AS m " +
                "INNER JOIN MPA ON MPA.id = m.mpa_id " +
                "LEFT JOIN likes AS l ON l.film_id = m.id " +
                "GROUP BY m.id " +
                "ORDER BY COUNT(l.user_id) DESC";

        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public List<Film> getDirectorFilmsSortedByYear(Long directorId) {
        String sqlQuery = "SELECT * " +
                "FROM MOVIE M " +
                "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                "WHERE M.ID IN (" +
                "SELECT FILM_ID " +
                "FROM FILM_DIRECTOR " +
                "WHERE DIRECTOR_ID = ?) " +
                "ORDER BY M.RELEASE_DATE";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getDirectorFilmsSortedByLikes(Long directorId) {
        String sqlQuery = "SELECT * " +
                "FROM MOVIE M " +
                "LEFT JOIN MPA ON MPA.ID = M.MPA_ID " +
                "LEFT JOIN LIKES L ON L.FILM_ID = M.ID " +
                "WHERE M.ID IN (" +
                "SELECT FILM_ID " +
                "FROM FILM_DIRECTOR " +
                "WHERE DIRECTOR_ID = ?) " +
                "GROUP BY M.ID " +
                "ORDER BY COUNT(L.USER_ID) " +
                "DESC";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name((resultSet.getString("name")))
                .releaseDate((resultSet.getDate("release_date")).toLocalDate())
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(Mpa.builder()
                        .id(resultSet.getLong("mpa.id"))
                        .name(resultSet.getString("mpa.name"))
                        .build())
                .genres(new LinkedHashSet<>())
                .directors(new LinkedHashSet<>())
                .build();
    }
}
