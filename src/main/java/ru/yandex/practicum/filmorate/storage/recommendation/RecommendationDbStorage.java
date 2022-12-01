package ru.yandex.practicum.filmorate.storage.recommendation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.film.FilmDbStorage.mapRowToFilm;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RecommendationDbStorage implements RecommendationStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getUserRecommendations(Long id) {
        List<Film> recommendations = new ArrayList<>();
        String sqlQueryForSimilarUser = "SELECT user_id, COUNT(film_id) AS same_film_count " +
                "FROM marks " +
                "WHERE film_id IN (SELECT film_id " +
                    "FROM marks " +
                    "WHERE user_id = ? AND mark > 5) AND user_id != ? " +
                "GROUP BY user_id " +
                "ORDER BY same_film_count DESC " +
                "LIMIT 1";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQueryForSimilarUser, id, id);
        if (!rowSet.next()) {
            return recommendations;
        }
        Long similarUserId = rowSet.getLong("user_id");
        String sqlQueryForRecommendations = "SELECT m.*, MPA.*, IFNULL(AVG(mr.MARK), 0) AS RATE " +
                "FROM MOVIE AS m " +
                "LEFT JOIN MARKS AS mr ON m.ID = mr.FILM_ID " +
                "INNER JOIN MPA ON MPA.ID = m.MPA_ID " +
                "WHERE mr.film_id NOT IN (SELECT film_id " +
                "FROM marks " +
                "WHERE user_id = ?) AND mr.user_id = ? " +
                "GROUP BY m.ID ";
        return jdbcTemplate.query(sqlQueryForRecommendations,
                (rs, rowNum) -> mapRowToFilm(rs),
                id, similarUserId);
    }
}
