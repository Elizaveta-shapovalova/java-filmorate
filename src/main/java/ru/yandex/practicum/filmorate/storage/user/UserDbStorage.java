package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("userStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> findAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs));
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
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , user.getBirthday()
                , user.getId());
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        String sqlQuery = "SELECT * FROM USERS WHERE id = ?";

        return jdbcTemplate.query(sqlQuery,(rs, rowNum) -> mapRowToUser(rs), userId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(String.format("User with %d id not found", userId)));
    }

    public static User mapRowToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
