package ru.yandex.practicum.filmorate.storage.like.follow;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface FollowStorage {
    void addFriend(Long id, Long friendId);
    void deleteFriend(Long id, Long friendId);
    List<User> getFriends(Long id);
    List<User> getCommonFriends(Long id, Long otherId);
}
