package ru.yandex.practicum.filmorate.DbTests;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.like.follow.FollowDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FollowDbTest {
    JdbcTemplate jdbcTemplate;
    FollowDbStorage followDbStorage;
    UserDbStorage userDbStorage;

    private User getUser() {
        return User.builder().email("test@mail.ru").login("testLogin").name("testName").birthday(Date.valueOf("1946-08-20").toLocalDate()).build();
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FOLLOW");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN ID RESTART WITH 1");
    }

    @Test
    public void testAddFriend() {
        userDbStorage.create(getUser());
        User user = getUser();
        user.setEmail("test1@mail.ru");
        user.setLogin("testLogin1");
        userDbStorage.create(user);
        assertDoesNotThrow(() -> followDbStorage.addFriend(1L, 2L));
    }

    @Test
    void testAddUnknownFriend() {
        userDbStorage.create(getUser());
        assertThrows(DataIntegrityViolationException.class, () -> followDbStorage.addFriend(1L, -1L), "Пользователь с id " + -1L + " не найден.");
    }

    @Test
    void testGetOneUserFriend() {
        userDbStorage.create(getUser());
        User user = getUser();
        user.setEmail("test1@mail.ru");
        user.setLogin("testLogin1");
        User savedUser = userDbStorage.create(user);
        followDbStorage.addFriend(1L, 2L);
        assertEquals(List.of(savedUser), followDbStorage.getFriends(1L));
    }

    @Test
    void testGetTwoUserFriends() {
        userDbStorage.create(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        User savedUser = userDbStorage.create(user1);
        User user2 = getUser();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("testLogin2");
        User savedUser2 = userDbStorage.create(user2);

        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(1L, 3L);
        assertEquals(List.of(savedUser, savedUser2), followDbStorage.getFriends(1L));
    }

    @Test
    void testGetEmptyFriendsOfFriend() {
        userDbStorage.create(getUser());
        User user = getUser();
        user.setEmail("test1@mail.ru");
        user.setLogin("testLogin1");
        userDbStorage.create(user);
        followDbStorage.addFriend(1L, 2L);
        assertEquals(new ArrayList<>(), followDbStorage.getFriends(2L));
    }

    @Test
    void testGetEmptyCommonFriends() {
        assertEquals(new ArrayList<>(), followDbStorage.getCommonFriends(1L, 2L));
    }

    @Test
    void testGetOneCommonFriend() {
        userDbStorage.create(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        userDbStorage.create(user1);
        User user2 = getUser();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("testLogin2");
        User savedUser2 = userDbStorage.create(user2);

        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(1L, 3L);
        followDbStorage.addFriend(2L, 3L);
        assertEquals(List.of(savedUser2), followDbStorage.getCommonFriends(1L, 2L));
    }

    @Test
    void testDeleteUserFriend() {
        userDbStorage.create(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        userDbStorage.create(user1);

        followDbStorage.addFriend(1L, 2L);
        assertDoesNotThrow(() -> followDbStorage.deleteFriend(1L, 2L));
    }

    @Test
    void testGetOneCommonFriendAfterDeletingOfFriend() {
        userDbStorage.create(getUser());
        User user1 = getUser();
        user1.setEmail("test1@mail.ru");
        user1.setLogin("testLogin1");
        userDbStorage.create(user1);
        User user2 = getUser();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("testLogin2");
        User savedUser2 = userDbStorage.create(user2);

        followDbStorage.addFriend(1L, 2L);
        followDbStorage.addFriend(1L, 3L);
        followDbStorage.addFriend(2L, 3L);
        followDbStorage.deleteFriend(1L, 2L);
        assertEquals(List.of(savedUser2), followDbStorage.getCommonFriends(1L, 2L));
    }
}