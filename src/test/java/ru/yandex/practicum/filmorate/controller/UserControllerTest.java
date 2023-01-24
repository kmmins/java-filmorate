package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {

    private UserController userController;
    @BeforeEach
    public void startController() {
        userController = new UserController();
    }

    @Test
    void checkAddUser() {
        var user1 = new User(1);
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        var user0 = new User(1);
        user0.setEmail("user0@user.com");
        user0.setLogin("login0");
        user0.setName("username0");
        user0.setBirthday(LocalDate.of(1989, 4, 20));
        var user2 = new User(2);
        user2.setEmail("user2.com");
        user2.setLogin("login2");
        user2.setName("username2");
        user2.setBirthday(LocalDate.of(1989, 4, 21));
        var user3 = new User(3);
        user3.setEmail("user3@user.com");
        user3.setLogin(" ");
        user3.setName("username3");
        user3.setBirthday(LocalDate.of(1989, 4, 22));
        var user4 = new User(4);
        user4.setEmail("user4@user.com");
        user4.setLogin("userlogin4");
        user4.setName("username4");
        user4.setBirthday(LocalDate.of(2024, 4, 23));
        var user5 = new User(5);
        user5.setEmail("user5@user.com");
        user5.setLogin("login5");
        user5.setName("");
        user5.setBirthday(LocalDate.of(1989, 4, 24));

        userController.addUser(user1);
        final UserAlreadyExistException e0 = assertThrows(UserAlreadyExistException.class,
                () -> userController.addUser(user0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> userController.addUser(user2));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> userController.addUser(user3));
        final ValidationException e3 = assertThrows(ValidationException.class, () -> userController.addUser(user4));
        userController.addUser(user5);
        var getAll = userController.getUsers();
        var checkUser5 = getAll.get(1);

        assertEquals("Не возможно добавить пользователя. Пользователь c таким id уже существует.", e0.getMessage());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ — @.", e1.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы.", e2.getMessage());
        assertEquals("Дата рождения не может быть в будущем.", e3.getMessage());
        assertEquals("login5", checkUser5.getName(), "Некорректное имя пользователя.");
    }

    @Test
    void checkUpdUser() {
        var user1 = new User(1);
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        var user0 = new User(10);
        user0.setEmail("user0@user.com");
        user0.setLogin("login0");
        user0.setName("username0");
        user0.setBirthday(LocalDate.of(1989, 4, 20));
        var user1upd1 = new User(1);
        user1upd1.setEmail("");
        user1upd1.setLogin("login1Upd1");
        user1upd1.setName("username1Upd1");
        user1upd1.setBirthday(LocalDate.of(1988, 4, 20));
        var user1upd2 = new User(1);
        user1upd2.setEmail("user1@userUpd2.com");
        user1upd2.setLogin("  ");
        user1upd2.setName("username1Upd2");
        user1upd2.setBirthday(LocalDate.of(1988, 4, 20));
        var user1upd3 = new User(1);
        user1upd3.setEmail("user1@userUpd3.com");
        user1upd3.setLogin("login1Upd3");
        user1upd3.setName("username1Upd3");
        user1upd3.setBirthday(LocalDate.of(2024, 4, 20));

        userController.addUser(user1);
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
        var user1 = new User(1);
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        var user2 = new User(2);
        user2.setEmail("user2@user.com");
        user2.setLogin("login2");
        user2.setName("username2");
        user2.setBirthday(LocalDate.of(1989, 4, 21));

        userController.addUser(user1);
        userController.addUser(user2);
        var getAll = userController.getUsers();

        assertNotNull(getAll, "Метод вернул null");
        assertFalse(getAll.isEmpty(), "Список фильмов пуст");
        assertEquals(2, getAll.size(), "Неверное количество фильмов.");
        assertEquals(user1, getAll.get(0), "Фильмы не совпадают.");
        assertEquals(user2, getAll.get(1), "Фильмы не совпадают.");
    }
}
