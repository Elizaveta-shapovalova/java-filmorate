package ru.yandex.practicum.filmorate.storage.mark;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

@Repository
@RequiredArgsConstructor
public class MarkDbStorage implements MarkStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void add(Long filmId, Long userId, Integer mark) {
        String sqlQuery = "MERGE INTO MARKS (film_id, user_id, mark) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.update(sqlQuery, filmId, userId, mark);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(
                    String.format("Film(%d) or User(%d) not found", filmId, userId)
            );
        }
    }

    @Override
    public void remove(Long filmId, Long userId) {
        String sqlQuery = "DELETE FROM MARKS WHERE film_id = ? AND user_id = ?";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0) {
            throw new ObjectNotFoundException(
                    String.format("Mark with Film(%d) and User(%d) not found", filmId, userId)
            );
        }
    }
}
