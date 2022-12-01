package ru.yandex.practicum.filmorate.controller.review;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.review.Review;
import ru.yandex.practicum.filmorate.service.review.ReviewService;

import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ReviewController {
    ReviewService reviewService;

    @GetMapping
    public Collection<Review> getAll(@RequestParam(name = "filmId", required = false) Optional<Long> filmId,
                                     @RequestParam(name = "count", required = false) Optional<Integer> count) {
        return reviewService.getAll(filmId, count);
    }

    @GetMapping("/{reviewId}")
    public Review getById(@Positive @PathVariable("reviewId") Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @PostMapping
    public Review create(@Validated(Marker.OnCreate.class) @RequestBody Review review) {
        return reviewService.create(review);
    }

    @PutMapping
    public Review update(@Validated(Marker.OnUpdate.class) @RequestBody Review review) {
        return reviewService.update(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteById(@Positive @PathVariable("reviewId") Long reviewId) {
        reviewService.deleteById(reviewId);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@Positive @PathVariable("reviewId") Long reviewId,
                        @Positive @PathVariable("userId") Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@Positive @PathVariable("reviewId") Long reviewId,
                           @Positive @PathVariable("userId") Long userId) {
        reviewService.addDislike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void deleteLike(@Positive @PathVariable("reviewId") Long reviewId,
                           @Positive @PathVariable("userId") Long userId) {
        reviewService.deleteLike(reviewId, userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void deleteDislike(@Positive @PathVariable("reviewId") Long reviewId,
                              @Positive @PathVariable("userId") Long userId) {
        reviewService.deleteDislike(reviewId, userId);
    }
}
