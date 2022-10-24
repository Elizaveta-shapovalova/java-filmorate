package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    public List<User> findAll();

    public User create(User user);

    public User update(User user);

    public User findUserById(Long userId);
}
