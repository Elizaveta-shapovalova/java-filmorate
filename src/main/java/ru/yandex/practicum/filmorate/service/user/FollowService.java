package ru.yandex.practicum.filmorate.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.event.Event;
import ru.yandex.practicum.filmorate.model.event.enums.EventType;
import ru.yandex.practicum.filmorate.model.event.enums.Operation;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.feed.FeedService;
import ru.yandex.practicum.filmorate.storage.like.follow.FollowStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowService {
    FollowStorage followStorage;
    UserService userService;
    FeedService feedService;

    public void addFriend(Long id, Long friendId) {
        userService.getById(id);
        userService.getById(friendId);
        followStorage.addFriend(id, friendId);
        feedService.addEvent(Event.createEvent(id, EventType.FRIEND, Operation.ADD, friendId));
    }

    public void deleteFriend(Long id, Long friendId) {
        followStorage.deleteFriend(id, friendId);
        feedService.addEvent(Event.createEvent(id, EventType.FRIEND, Operation.REMOVE, friendId));
    }

    public List<User> getFriends(Long id) {
        userService.getById(id);
        return followStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        userService.getById(id);
        userService.getById(otherId);
        return followStorage.getCommonFriends(id, otherId);
    }
}
