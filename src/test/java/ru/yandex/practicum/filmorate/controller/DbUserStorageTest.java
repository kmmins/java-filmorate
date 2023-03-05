package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest {

    private final DbUserStorage userStorage;

    @Test
    public void checkAddUser() {
        var user1 = new User();
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1978, 4, 20));
        var userError = new User();
        userError.setEmail("user1@user.com");
        userError.setLogin("loginError");
        userError.setName("usernameError");
        userError.setBirthday(LocalDate.of(1978, 4, 20));

        var addedUser1 = userStorage.add(user1);
        var checkSize = userStorage.getAll().size();

        assertEquals(1, checkSize, "Некорректное количество пользователей.");
        assertEquals(1, addedUser1.getId(), "Некорректный id");
        assertEquals("user1@user.com", addedUser1.getEmail(), "Неправильная почта");
        final UserAlreadyExistException e = assertThrows(UserAlreadyExistException.class,
                () -> userStorage.add(userError));
        var checkSizeAfter = userStorage.getAll().size();
        assertEquals(1, checkSizeAfter, "Некорректное количество пользователей.");
    }

    @Test
    public void checkGetAllUser() {
        var user4 = new User();
        user4.setEmail("user4@user.com");
        user4.setLogin("login4");
        user4.setName("username4");
        user4.setBirthday(LocalDate.of(1978, 4, 20));
        var user5 = new User();
        user5.setEmail("user5@user.com");
        user5.setLogin("login5");
        user5.setName("username5");
        user5.setBirthday(LocalDate.of(1978, 4, 20));

        var checkSize = userStorage.getAll().size();

        assertEquals(3, checkSize, "Некорректное количество пользователей.");
        var addedUser4 = userStorage.add(user4);
        var addedUser5 = userStorage.add(user5);
        var checkSizeAfter = userStorage.getAll().size();
        assertEquals(checkSize + 2, checkSizeAfter, "Некорректное количество пользователей.");
    }

    @Test
    public void checkGetByIdUser() {
        var user6 = new User();
        user6.setEmail("user6@user.com");
        user6.setLogin("login6");
        user6.setName("username6");
        user6.setBirthday(LocalDate.of(1978, 4, 20));

        var checkSize = userStorage.getAll().size();
        assertEquals(5, checkSize, "Некорректное количество пользователей.");
        var addedUser6 = userStorage.add(user6);
        var checkSizeAfter = userStorage.getAll().size();
        assertEquals(checkSize + 1, checkSizeAfter, "Некорректное количество пользователей.");

        Optional<User> userOptional = Optional.ofNullable(userStorage.getById(addedUser6.getId()));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("id", 6)
                );
    }

    @Test
    public void checkUpdateUser() {
        var user2 = new User();
        user2.setEmail("user2@user.com");
        user2.setLogin("login2");
        user2.setName("username2");
        user2.setBirthday(LocalDate.of(1978, 4, 20));
        var user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin("login3");
        user3.setName("username3");
        user3.setBirthday(LocalDate.of(1978, 4, 20));
        var addedUser2 = userStorage.add(user2);
        var addedUser3 = userStorage.add(user3);
        assertEquals(new HashMap<>(), addedUser2.getFriendsMap(), "Не пустая мапа");
        assertEquals(2, addedUser2.getId(), "ПОЧЕМУ ????????");
        HashMap<Integer, Boolean> testMap = new HashMap<>();
        testMap.put(3, true);

        addedUser2.getFriendsMap().put(addedUser3.getId(), true);
        var user2AfterUpd = userStorage.update(addedUser2);

        assertEquals(testMap, user2AfterUpd.getFriendsMap(), "Пользователя нет в друзьях");
    }
}
