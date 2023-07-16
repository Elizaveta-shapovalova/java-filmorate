package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MpaDbStorage implements MpaStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA);
    }

    @Override
    public Mpa getById(Long id) {
        String sqlQuery = "SELECT * FROM MPA WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMPA, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("MPA with %d id not found", id)));
    }

    private Mpa mapRowToMPA(ResultSet resultSet, int nowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
