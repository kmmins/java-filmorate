package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest {

    private final DbUserStorage userStorage;

    @Test
    public void checkAddUser() {
        var user1Db = new User();
        user1Db.setEmail("user1Db@user.com");
        user1Db.setLogin("login1Db");
        user1Db.setName("username1Db");
        user1Db.setBirthday(LocalDate.of(1978, 4, 20));
        var checkSize = userStorage.getAll().size();

        var addedUser1Db = userStorage.add(user1Db);

        assertEquals(checkSize + 1, addedUser1Db.getId(), "Некорректный id.");
        assertEquals("user1Db@user.com", addedUser1Db.getEmail(), "Некорректная почта.");
    }

    @Test
    public void checkUpdateUser() {
        var user2Db = new User();
        user2Db.setEmail("user2Db@user.com");
        user2Db.setLogin("login2Db");
        user2Db.setName("username2Db");
        user2Db.setBirthday(LocalDate.of(1978, 4, 20));
        var user3Db = new User();
        user3Db.setEmail("user3Db@user.com");
        user3Db.setLogin("login3Db");
        user3Db.setName("username3Db");
        user3Db.setBirthday(LocalDate.of(1978, 4, 20));
        var checkSize = userStorage.getAll().size();
        var addedUser2Db = userStorage.add(user2Db);
        var addedUser3Db = userStorage.add(user3Db);
        HashMap<Integer, Boolean> testMap = new HashMap<>();
        testMap.put(checkSize + 2, true);
        addedUser2Db.getFriendsMap().put(addedUser3Db.getId(), true);

        var user2AfterUpdDb = userStorage.update(addedUser2Db);

        assertEquals(testMap, user2AfterUpdDb.getFriendsMap(), "Пользователя нет в друзьях.");
    }

    @Test
    public void checkGetAllUser() {
        var user4Db = new User();
        user4Db.setEmail("user4Db@user.com");
        user4Db.setLogin("login4Db");
        user4Db.setName("username4Db");
        user4Db.setBirthday(LocalDate.of(1978, 4, 20));
        var user5Db = new User();
        user5Db.setEmail("user5Db@user.com");
        user5Db.setLogin("login5Db");
        user5Db.setName("username5Db");
        user5Db.setBirthday(LocalDate.of(1978, 4, 20));
        var checkSize = userStorage.getAll().size();
        userStorage.add(user4Db);
        userStorage.add(user5Db);

        var checkResult = userStorage.getAll();

        assertEquals(checkSize + 2, checkResult.size(), "Некорректное количество пользователей.");
    }

    @Test
    public void checkGetByIdUser() {
        var user6Db = new User();
        user6Db.setEmail("user6Db@user.com");
        user6Db.setLogin("login6Db");
        user6Db.setName("username6Db");
        user6Db.setBirthday(LocalDate.of(1978, 4, 20));
        var checkSize = userStorage.getAll().size();
        var addedUser6Db = userStorage.add(user6Db);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(addedUser6Db.getId()));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", checkSize + 1)
                                .hasFieldOrPropertyWithValue("email", user.getEmail())
                                .hasFieldOrPropertyWithValue("login", user.getLogin())
                                .hasFieldOrPropertyWithValue("name", user.getName())
                                .hasFieldOrPropertyWithValue("birthday", user.getBirthday())
                                .hasFieldOrPropertyWithValue("friendsMap", user.getFriendsMap())
                );
    }
}
