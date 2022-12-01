package ru.yandex.practicum.filmorate.storage.review.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewLikeDbStorage implements ReviewLikeStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(Long reviewId, Long userId) {
        String sqlQuery = "MERGE INTO REVIEW_LIKE (REVIEW_ID, USER_ID, IS_POSITIVE) VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, 1);
    }

    @Override
    public void deleteLike(Long reviewId, Long userId) {
        String sqlQuery = "DELETE FROM REVIEW_LIKE WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_POSITIVE = ?";
        if (jdbcTemplate.update(sqlQuery, reviewId, userId, 1) == 0) {
            throw new ObjectNotFoundException(
                    String.format("Review like with %d id not found", reviewId));
        }
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        try {
            String sqlQuery = "MERGE INTO REVIEW_LIKE (REVIEW_ID, USER_ID, IS_POSITIVE) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, reviewId, userId, -1);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(
                    String.format("Review with %d id or User with %d id not found",
                            reviewId,
                            userId)
            );
        }
    }

    @Override
    public void deleteDislike(Long reviewId, Long userId) {
        String sqlQuery = "DELETE FROM REVIEW_LIKE WHERE REVIEW_ID = ? AND USER_ID = ? AND IS_POSITIVE = ?";
        if (jdbcTemplate.update(sqlQuery, reviewId, userId, -1) == 0) {
            throw new ObjectNotFoundException(
                    String.format("Review like with %d id not found", reviewId));
        }
    }
}
