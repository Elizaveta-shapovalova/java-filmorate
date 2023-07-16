package ru.yandex.practicum.filmorate.controller.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.FollowService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users/{id}/friends")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowController {
    FollowService followService;

    @PutMapping("/{friendId}")
    public void addFriend(@Positive @PathVariable("id") Long id, @Positive @PathVariable("friendId") Long friendId) {
        followService.addFriend(id, friendId);
    }

    @DeleteMapping("/{friendId}")
    public void deleteFriend(@Positive @PathVariable("id") Long id, @Positive @PathVariable("friendId") Long friendId) {
        followService.deleteFriend(id, friendId);
    }

    @GetMapping
    public List<User> getFriends(@Positive @PathVariable("id") Long id) {
        return followService.getFriends(id);
    }

    @GetMapping("/common/{otherId}")
    public List<User> getCommonFriends(@Positive @PathVariable("id") Long id, @Positive @PathVariable("otherId") Long otherId) {
        return followService.getCommonFriends(id, otherId);
    }
}
