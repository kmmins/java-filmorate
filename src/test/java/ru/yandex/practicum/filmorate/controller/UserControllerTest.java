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

    private final UserController userController;

    @Autowired
    public UserControllerTest(UserController userController) {
        this.userController = userController;
    }

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
        var getAll = userController.getAllUsers();
        var checkUser5 = getAll.get(1);

        assertEquals("addUser.user.email: должно иметь формат адреса электронной почты", e1.getMessage());
        assertEquals("addUser.user.login: не должно быть пустым", e2.getMessage());
        assertEquals("addUser.user.birthday: должно содержать прошедшую дату", e3.getMessage());
        assertEquals("login5", checkUser5.getName(), "Некорректное имя пользователя.");
    }

    @Test
    void checkUpdUser() {
        var user2 = new User();
        user2.setEmail("user2@user.com");
        user2.setLogin("login2");
        user2.setName("username2");
        user2.setBirthday(LocalDate.of(1989, 4, 21));
        var addedUser2 = userController.addUser(user2);

        var user0 = new User(100500);
        user0.setEmail("user0@user.com");
        user0.setLogin("login0");
        user0.setName("username0");
        user0.setBirthday(LocalDate.of(1989, 4, 21));

        var user2upd1 = new User(addedUser2.getId());
        user2upd1.setEmail(" ");
        user2upd1.setLogin("login2Upd1");
        user2upd1.setName("username2Upd1");
        user2upd1.setBirthday(LocalDate.of(1988, 4, 21));
        var user2upd2 = new User(addedUser2.getId());
        user2upd2.setEmail("user2@userUpd2.com");
        user2upd2.setLogin("  ");
        user2upd2.setName("username2Upd2");
        user2upd2.setBirthday(LocalDate.of(1988, 4, 21));
        var user2upd3 = new User(addedUser2.getId());
        user2upd3.setEmail("user2@userUpd3.com");
        user2upd3.setLogin("login2Upd3");
        user2upd3.setName("username2Upd3");
        user2upd3.setBirthday(LocalDate.of(2024, 4, 21));

        final UserNotFoundException e0 = assertThrows(UserNotFoundException.class, () -> userController.updUser(user0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> userController.updUser(user2upd1));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> userController.updUser(user2upd2));
        final ValidationException e3 = assertThrows(ValidationException.class, () -> userController.updUser(user2upd3));

        assertEquals("Не возможно обновить данные пользователя. Такого пользователя с таким id не существует.",
                e0.getMessage());
        assertEquals("updUser.user.email: должно иметь формат адреса электронной почты", e1.getMessage());
        assertEquals("updUser.user.login: не должно быть пустым", e2.getMessage());
        assertEquals("updUser.user.birthday: должно содержать прошедшую дату", e3.getMessage());
    }

    @Test
    void checkGetUsers() {
        var getAllBefore = userController.getAllUsers();
        var sizeBefore = getAllBefore.size();
        var user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin("login3");
        user3.setName("username3");
        user3.setBirthday(LocalDate.of(1989, 4, 23));
        var addedUser3 = userController.addUser(user3);
        var user4 = new User();
        user4.setEmail("user4@user.com");
        user4.setLogin("login4");
        user4.setName("username4");
        user4.setBirthday(LocalDate.of(1989, 4, 24));
        var addedUser4 = userController.addUser(user4);

        var getAllAfter = userController.getAllUsers();
        var sizeAfter = getAllAfter.size();

        assertNotNull(getAllAfter, "Метод вернул null");
        assertFalse(getAllAfter.isEmpty(), "Список пользователей пуст");
        assertEquals(sizeBefore + 2, sizeAfter, "Неверное количество пользователей.");
        assertEquals(addedUser3, getAllAfter.get(sizeBefore), "Фильмы не совпадают.");
        assertEquals(addedUser4, getAllAfter.get(sizeBefore + 1), "Фильмы не совпадают.");
    }
}
