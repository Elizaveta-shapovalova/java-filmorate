package ru.yandex.practicum.filmorate.storage.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("userStorage")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;

    public static User mapRowToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs));
    }

    @Override
    public User getById(Long userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE id = ?";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User with %d id not found", userId)));
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        user.setId(simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET " +
                "EMAIL = ?, LOGIN = ?, NAME = ? , BIRTHDAY = ?" +
                "WHERE ID = ?";
        if (jdbcTemplate.update(sqlQuery, user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId()) == 0) {
            throw new ObjectNotFoundException(String.format("User with %d id not found", user.getId()));
        }
        return user;
    }

    @Override
    public void deleteById(Long userId) {
        String sqlQuery = "DELETE FROM users WHERE id = ?";
        if (jdbcTemplate.update(sqlQuery, userId) == 0) {
            throw new ObjectNotFoundException(String.format("User with %d id not found", userId));
        }
    }
}
