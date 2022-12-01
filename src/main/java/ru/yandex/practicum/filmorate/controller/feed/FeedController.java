package ru.yandex.practicum.filmorate.controller.feed;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.service.feed.FeedService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{id}/feed")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeedController {
    FeedService feedService;

    @GetMapping
    public List<Event> getEventsByUserId(@Positive @PathVariable("id") Long id) {
        return feedService.getEventsByUserId(id);
    }
}
