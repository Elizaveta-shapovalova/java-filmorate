package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.review.ReviewLikeStorage;
import ru.yandex.practicum.filmorate.storage.review.impl.ReviewDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewDbTest {
    JdbcTemplate jdbcTemplate;
    ReviewDbStorage reviewStorage;
    ReviewLikeStorage likeStorage;
    FilmService filmService;
    UserService userService;

    @BeforeAll
    public void beforeAll() {
        userService.create(User.builder()
                .email("1@ya.ru")
                .login("bot")
                .name("Vova")
                .birthday(LocalDate.of(2022, 1, 1))
                .build());
        filmService.create(Film.builder()
                .name("Iron Man")
                .description("Some superhero's shit")
                .releaseDate(LocalDate.of(2012, 8, 12))
                .duration(120)
                .mpa(Mpa.builder().id(2L).name("PG").build())
                .build());
    }

    private Review getReview() {
        return Review.builder()
                .content("good review")
                .positive(true)
                .filmId(1L)
                .userId(1L)
                .useful(0L)
                .build();
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.update("DELETE FROM reviews");
        jdbcTemplate.update("ALTER TABLE REVIEWS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void testCreateReview() {
        var review = getReview();
        reviewStorage.create(review);
        assertEquals(review, reviewStorage.getById(review.getReviewId()));
    }

    @Test
    public void testUpdateReview() {
        var review = getReview();
        reviewStorage.create(review);
        review.setContent("updated good review");
        reviewStorage.update(review);
        assertEquals(review, reviewStorage.getById(review.getReviewId()));
    }

    @Test
    public void testFindReviewByWrongId() {
        assertThrows(ObjectNotFoundException.class, () -> reviewStorage.getById(-1L));
    }

    @Test
    public void testDeleteReview() {
        var review = getReview();
        reviewStorage.create(review);
        reviewStorage.deleteById(review.getReviewId());
        assertEquals(0, reviewStorage.getAll().size());
        assertThrows(ObjectNotFoundException.class, () -> reviewStorage.getById(review.getReviewId()));
    }

    @Test
    public void testFindAllReviews() {
        var review = getReview();
        var review1 = getReview();
        var review2 = getReview();
        List<Review> values = new ArrayList<>();

        values.add(reviewStorage.create(review));
        values.add(reviewStorage.create(review1));
        values.add(reviewStorage.create(review2));

        assertEquals(values, reviewStorage.getAll());
    }

    @Test
    public void testFindAllReviewsWithCount() {
        var review = reviewStorage.create(getReview());
        reviewStorage.create(getReview());
        reviewStorage.create(getReview());
        List<Review> values = new ArrayList<>();

        values.add(review);

        assertEquals(values, reviewStorage.getAll(1L, 1));
    }

    @Test
    public void testAddReviewLikeAndDelete() {
        var review = reviewStorage.create(getReview());
        var user = userService.getById(1L);

        likeStorage.addLike(review.getReviewId(), user.getId());

        assertEquals(1, reviewStorage.getById(1L).getUseful());

        likeStorage.deleteLike(review.getReviewId(), user.getId());

        assertEquals(0, reviewStorage.getById(1L).getUseful());
    }

    @Test
    public void testAddReviewDislikeAndDelete() {
        var review = reviewStorage.create(getReview());
        var user = userService.getById(1L);

        likeStorage.addDislike(review.getReviewId(), user.getId());

        assertEquals(-1, reviewStorage.getById(1L).getUseful());

        likeStorage.deleteDislike(review.getReviewId(), user.getId());

        assertEquals(0, reviewStorage.getById(1L).getUseful());
    }
}
