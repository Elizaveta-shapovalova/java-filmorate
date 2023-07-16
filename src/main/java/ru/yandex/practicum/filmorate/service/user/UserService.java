package ru.yandex.practicum.filmorate.service.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {
    UserStorage userStorage;

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User create(User user) {
        setUserNameAsLoginIfEmpty(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        setUserNameAsLoginIfEmpty(user);
        return userStorage.update(user);
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public void deleteById(Long userId) {
        userStorage.deleteById(userId);
    }

    private void setUserNameAsLoginIfEmpty(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
