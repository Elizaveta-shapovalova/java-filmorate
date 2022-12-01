package ru.yandex.practicum.filmorate.storage.like;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LikeDbStorage implements LikeStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long userId, Long filmId) {
        String sqlQuery = "MERGE INTO LIKES key(FILM_ID,USER_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(Long userId, Long filmId) {
        String sqlQuery = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        if (jdbcTemplate.update(sqlQuery, filmId, userId) == 0) {
            throw new ObjectNotFoundException(String.format("Like of user with %d and film with %d id not found",
                    userId, filmId));
        }
    }

    @Override
    public List<Like> getLikes(Long userId, Long filmId) {
        String sqlQuery = "SELECT * FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?";
        List<Like> likes;
        try {
            likes = jdbcTemplate.query(sqlQuery, this::mapRowToLike, userId, filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Like not found");
        }
        return likes;
    }

    private Like mapRowToLike(ResultSet resultSet, int nowNum) throws SQLException {
        return Like.builder()
                .filmId(resultSet.getLong("film_id"))
                .userId(resultSet.getLong("user_id"))
                .build();
    }
}
