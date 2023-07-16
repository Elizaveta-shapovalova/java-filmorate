package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;
import java.util.List;

public interface UserStorage {
    List<User> getAll();
    User getById(Long id);
    User create(User user);
    User update(User user);
    void deleteById(Long userId);
}
