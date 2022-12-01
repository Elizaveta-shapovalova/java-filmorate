package ru.yandex.practicum.filmorate.storage.genre;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GenreDbStorage implements GenreStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Genre getById(Long genreId) {
        String sqlQuery = "SELECT * FROM GENRE WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToGenre(rs), genreId).stream().findFirst().orElseThrow(() -> new ObjectNotFoundException(String.format("Genre with %d id not found", genreId)));
    }

    @Override
    public void addGenresToFilm(Long filmId, LinkedHashSet<Genre> genres) {
        List<Genre> genreList = new ArrayList<>(genres);
        jdbcTemplate.batchUpdate("MERGE INTO film_genre key(FILM_ID,genre_id) values (?, ?)", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement statement, int i) throws SQLException {
                statement.setLong(1, filmId);
                statement.setLong(2, genreList.get(i).getId());
            }

            public int getBatchSize() {
                return genreList.size();
            }
        });
    }

    @Override
    public void deleteFilmGenres(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void loadGenres(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));

        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        final String sqlQuery = "SELECT * FROM GENRE AS g, FILM_GENRE AS fg WHERE fg.GENRE_ID = g.ID AND fg.FILM_ID in (" + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs) -> {
            final Film film = filmById.get(rs.getLong("FILM_ID"));
            film.getGenres().add(mapRowToGenre(rs));
        }, films.stream().map(Film::getId).toArray());
    }

    private Genre mapRowToGenre(ResultSet resultSet) throws SQLException {
        return Genre.builder().id(resultSet.getLong("id")).name(resultSet.getString("name")).build();
    }
}
