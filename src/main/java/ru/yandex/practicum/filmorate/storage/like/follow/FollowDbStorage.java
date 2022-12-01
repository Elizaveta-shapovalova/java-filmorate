package ru.yandex.practicum.filmorate.storage.like.follow;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

import static ru.yandex.practicum.filmorate.storage.user.UserDbStorage.mapRowToUser;

@Repository
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowDbStorage implements FollowStorage {
    JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO FOLLOW(USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM FOLLOW WHERE USER_ID = ? AND FRIEND_ID = ?";
        if (jdbcTemplate.update(sqlQuery, id, friendId ) == 0) {
            throw new ObjectNotFoundException(String.format("Friends with %d and %d id not found", id, friendId));
        }
    }

    @Override
    public List<User> getFriends(Long id) {
        String sqlQuery = "SELECT * " +
                "FROM USERS AS U " +
                "INNER JOIN FOLLOW AS F ON U.ID = F.FRIEND_ID " +
                "WHERE F.USER_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sqlQuery = "SELECT * FROM USERS AS U " +
                "INNER JOIN FOLLOW AS F ON U.ID = F.FRIEND_ID " +
                "WHERE USER_ID = ? AND FRIEND_ID IN (SELECT FRIEND_ID FROM FOLLOW WHERE USER_ID = ?) ";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> mapRowToUser(rs), id, otherId);
    }
}
