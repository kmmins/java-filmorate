package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void checkAddUser() {
        var user1 = new User();
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        userController.addUser(user1);
        var user2 = new User();
        user2.setEmail("это-неправильный?эмейл@");
        user2.setLogin("login2");
        user2.setName("username2");
        user2.setBirthday(LocalDate.of(1989, 4, 21));
        var user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin(" ");
        user3.setName("username3");
        user3.setBirthday(LocalDate.of(1989, 4, 22));
        var user4 = new User();
        user4.setEmail("user4@user.com");
        user4.setLogin("userlogin4");
        user4.setName("username4");
        user4.setBirthday(LocalDate.of(2024, 4, 23));
        var user5 = new User();
        user5.setEmail("user5@user.com");
        user5.setLogin("login5");
        user5.setName("");
        user5.setBirthday(LocalDate.of(1989, 4, 24));
        userController.addUser(user5);

        final ValidationException e1 = assertThrows(ValidationException.class, () -> userController.addUser(user2));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> userController.addUser(user3));
        final ValidationException e3 = assertThrows(ValidationException.class, () -> userController.addUser(user4));
        var getAll = userController.getUsers();
        var checkUser5 = getAll.get(1);

        assertEquals("Электронная почта не может быть пустой и должна содержать символ — @.", e1.getMessage());
        assertEquals("addUser.user.login: не должно быть пустым", e2.getMessage());
        assertEquals("Дата рождения не может быть в будущем.", e3.getMessage());
        assertEquals("login5", checkUser5.getName(), "Некорректное имя пользователя.");
    }

    @Test
    void checkUpdUser() {
        var user1 = new User();
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        var addedUser = userController.addUser(user1);
        var user0 = new User(100500);
        user0.setEmail("user0@user.com");
        user0.setLogin("login0");
        user0.setName("username0");
        user0.setBirthday(LocalDate.of(1989, 4, 20));
        var user1upd1 = new User(addedUser.getId());
        user1upd1.setEmail("");
        user1upd1.setLogin("login1Upd1");
        user1upd1.setName("username1Upd1");
        user1upd1.setBirthday(LocalDate.of(1988, 4, 20));
        var user1upd2 = new User(addedUser.getId());
        user1upd2.setEmail("user1@userUpd2.com");
        user1upd2.setLogin("  ");
        user1upd2.setName("username1Upd2");
        user1upd2.setBirthday(LocalDate.of(1988, 4, 20));
        var user1upd3 = new User(addedUser.getId());
        user1upd3.setEmail("user1@userUpd3.com");
        user1upd3.setLogin("login1Upd3");
        user1upd3.setName("username1Upd3");
        user1upd3.setBirthday(LocalDate.of(2024, 4, 20));

        final UserNotFoundException e0 = assertThrows(UserNotFoundException.class, () -> userController.updUser(user0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> userController.updUser(user1upd1));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> userController.updUser(user1upd2));
        final ValidationException e3 = assertThrows(ValidationException.class, () -> userController.updUser(user1upd3));

        assertEquals("Не возможно обновить данные пользователя. Такого пользователя с таким id не существует.",
                e0.getMessage());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ — @.", e1.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы.", e2.getMessage());
        assertEquals("Дата рождения не может быть в будущем.", e3.getMessage());
    }

    @Test
    void checkGetUsers() {
        var getAll = userController.getUsers();
        var sizeBefore = getAll.size();
        var user1 = new User(1);
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        userController.addUser(user1);
        var user2 = new User(2);
        user2.setEmail("user2@user.com");
        user2.setLogin("login2");
        user2.setName("username2");
        user2.setBirthday(LocalDate.of(1989, 4, 21));
        userController.addUser(user2);

        getAll = userController.getUsers();

        assertNotNull(getAll, "Метод вернул null");
        assertFalse(getAll.isEmpty(), "Список фильмов пуст");
        assertEquals(sizeBefore + 2, getAll.size(), "Неверное количество фильмов.");
        assertEquals(user1, getAll.get(sizeBefore), "Фильмы не совпадают.");
        assertEquals(user2, getAll.get(sizeBefore + 1), "Фильмы не совпадают.");
    }
}
