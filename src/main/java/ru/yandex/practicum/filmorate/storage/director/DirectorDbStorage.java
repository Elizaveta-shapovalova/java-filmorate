package ru.yandex.practicum.filmorate.storage.director;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Director;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class DirectorDbStorage implements DirectorStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getAll() {
        String sqlQuery = "SELECT * FROM DIRECTORS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToDirector(rs));
    }

    @Override
    public Director getById(Long id) {
        String sqlQuery = "SELECT * FROM DIRECTORS WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToDirector(rs), id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Director with %d id not found", id)));
    }

    @Override
    public Director create(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");
        director.setId(simpleJdbcInsert.executeAndReturnKey(director.toMap()).longValue());
        return director;
    }

    @Override
    public Director update(Director director) {
        String sqlQuery = "UPDATE DIRECTORS SET NAME = ? WHERE ID = ?";
        if (jdbcTemplate.update(sqlQuery, director.getName(), director.getId()) == 0) {
            throw new ObjectNotFoundException(String.format("Director with %d id not found", director.getId()));
        }
        return director;
    }

    @Override
    public void deleteById(Long id) {
        String sqlQuery = "DELETE FROM DIRECTORS WHERE ID = ?";
        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            throw new ObjectNotFoundException(String.format("Director with %d id not found", id));
        }
    }

    @Override
    public void addFilmDirectors(Long filmId, LinkedHashSet<Director> directors) {
        List<Director> directorList = new ArrayList<>(directors);
        jdbcTemplate.batchUpdate(
                "MERGE INTO FILM_DIRECTOR KEY (FILM_ID, DIRECTOR_ID) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement statement, int i) throws SQLException {
                        statement.setLong(1, filmId);
                        statement.setLong(2, directorList.get(i).getId());
                    }

                    public int getBatchSize() {
                        return directorList.size();
                    }
                }
        );
    }

    @Override
    public void deleteFilmDirectors(Long filmId) {
        String sqlQuery = "DELETE FROM FILM_DIRECTOR WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void loadDirectors(List<Film> films) {
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        String sqlQuery = String.format("SELECT * FROM DIRECTORS D, FILM_DIRECTOR FD WHERE FD.DIRECTOR_ID = D.ID AND FD.FILM_ID IN (%s)", inSql);
        Map<Long, Film> filmMap = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        jdbcTemplate.query(
                sqlQuery,
                (rs) -> {
                    Film film = filmMap.get(rs.getLong("FILM_ID"));
                    film.getDirectors().add(mapRowToDirector(rs));
                },
                filmMap.keySet().toArray());
    }

    private Director mapRowToDirector(ResultSet resultSet) throws SQLException {
        return Director.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
