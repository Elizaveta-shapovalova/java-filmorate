package ru.yandex.practicum.filmorate.storage.review.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewDbStorage implements ReviewStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Review> getAll() {
        String sqlQuery = "SELECT R.*, IFNULL(SUM(RL.IS_POSITIVE), 0) AS USEFUL " +
                "FROM REVIEWS AS R " +
                "LEFT JOIN REVIEW_LIKE RL on R.ID = RL.REVIEW_ID " +
                "GROUP BY R.ID " +
                "ORDER BY USEFUL DESC";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview);
    }

    @Override
    public Collection<Review> getAll(Long filmId, Integer count) {
        if (filmId == -1) {
            String sqlQuery = "SELECT R.*, IFNULL(SUM(RL.IS_POSITIVE), 0) AS USEFUL " +
                    "FROM REVIEWS AS R " +
                    "LEFT JOIN REVIEW_LIKE RL on R.ID = RL.REVIEW_ID " +
                    "GROUP BY R.ID " +
                    "ORDER BY USEFUL DESC " +
                    "LIMIT ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToReview, count);
        }
        String sqlQuery = "SELECT R.*, IFNULL(SUM(RL.IS_POSITIVE), 0) AS USEFUL " +
                "FROM REVIEWS AS R " +
                "LEFT JOIN REVIEW_LIKE RL on R.ID = RL.REVIEW_ID " +
                "WHERE R.FILM_ID = ?" +
                "GROUP BY R.ID " +
                "ORDER BY USEFUL DESC " +
                "LIMIT ?";
        return new ArrayList<>(jdbcTemplate.query(sqlQuery, this::mapRowToReview, filmId, count));
    }

    @Override
    public Review getById(Long reviewId) {
        String sqlQuery = "SELECT R.*, SUM(RL.IS_POSITIVE) AS USEFUL " +
                "FROM REVIEWS AS R " +
                "LEFT JOIN REVIEW_LIKE RL on R.ID = RL.REVIEW_ID " +
                "WHERE R.ID = ? " +
                "GROUP BY R.ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToReview, reviewId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Review not found."));
    }

    @Override
    public Review create(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("REVIEWS")
                .usingGeneratedKeyColumns("ID");
        review.setReviewId(simpleJdbcInsert.executeAndReturnKey(review.toMap()).longValue());
        return review;
    }

    @Override
    public Review update(Review review) {
        String sqlQuery = "UPDATE REVIEWS SET " +
                "CONTENT = ?, IS_POSITIVE = ? " +
                "WHERE ID = ?";
        if (jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getPositive(),
                review.getReviewId())
                == 0) {
            throw new ObjectNotFoundException(
                    String.format("Review with %d id not found.", review.getReviewId()));
        }
        return review;
    }

    @Override
    public void deleteById(Long reviewId) {
        String sqlQuery = "DELETE FROM REVIEWS WHERE ID = ?";
        if (jdbcTemplate.update(sqlQuery, reviewId) == 0) {
            throw new ObjectNotFoundException(
                    String.format("Review with %d id not found.", reviewId));
        }
    }

    private Review mapRowToReview(ResultSet rs, int rowNum) throws SQLException {
        return Review.builder().reviewId(rs.getLong("ID"))
                .content(rs.getString("CONTENT"))
                .positive(rs.getBoolean("IS_POSITIVE"))
                .filmId(rs.getLong("FILM_ID"))
                .userId(rs.getLong("USER_ID"))
                .useful(rs.getLong("USEFUL"))
                .build();
    }
}
