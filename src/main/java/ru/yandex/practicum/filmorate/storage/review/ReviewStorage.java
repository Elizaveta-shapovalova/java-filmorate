package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.review.Review;

import java.util.Collection;

public interface ReviewStorage {
    Collection<Review> getAll();
    Collection<Review> getAll(Long filmId, Integer count);
    Review getById(Long reviewId);
    Review create(Review review);
    Review update(Review review);
    void deleteById(Long reviewId);
}
