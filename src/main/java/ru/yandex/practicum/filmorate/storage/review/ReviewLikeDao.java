package ru.yandex.practicum.filmorate.storage.review;

public interface ReviewLikeDao {
    long getUseful(long reviewId);
    void addLike(long reviewId, long userId);
    void addDislike(long reviewId, long userId);
    void removeLike(long reviewId, long userId);
    void removeDislike(long reviewId, long userId);
}
