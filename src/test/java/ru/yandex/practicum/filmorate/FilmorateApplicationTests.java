package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void checkAddFilm() {
        var filmController = new FilmController();
        var film1 = new Film(1);
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(Duration.ofMinutes(91));
        var film0 = new Film(1);
        film0.setName("фильм0");
        film0.setDescription("описание0");
        film0.setReleaseDate(LocalDate.of(2023, 1, 1));
        film0.setDuration(Duration.ofMinutes(91));
        var film2 = new Film(2);
        film2.setName("");
        film2.setDescription("описание2");
        film2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2.setDuration(Duration.ofMinutes(92));
        var film3 = new Film(3);
        film3.setName("фильм3");
        film3.setDescription("описание3------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------" +
                "больше 200 символов");
        film3.setReleaseDate(LocalDate.of(2023, 1, 3));
        film3.setDuration(Duration.ofMinutes(93));
        var film4 = new Film(4);
        film4.setName("фильм4");
        film4.setDescription("описание4");
        film4.setReleaseDate(LocalDate.of(1895, 12, 27));
        film4.setDuration(Duration.ofMinutes(94));
        var film5 = new Film(5);
        film5.setName("фильм5");
        film5.setDescription("описание5");
        film5.setReleaseDate(LocalDate.of(2023, 1, 5));
        film5.setDuration(Duration.ofMinutes(-95));

        filmController.addFilm(film1);
        final FilmAlreadyExistException e0 = assertThrows(FilmAlreadyExistException.class,
                () -> filmController.addFilm(film0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> filmController.addFilm(film3));
        final ValidationException e3 = assertThrows(ValidationException.class, () -> filmController.addFilm(film4));
        final ValidationException e4 = assertThrows(ValidationException.class, () -> filmController.addFilm(film5));

        assertEquals("Не возможно добавить фильм. Фильм с таким id уже существует.", e0.getMessage());
        assertEquals("Название фильма не может быть пустым.", e1.getMessage());
        assertEquals("Максимальная длина описания фильма — 200 символов.", e2.getMessage());
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", e3.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", e4.getMessage());
    }

    @Test
    void checkUpdFilm() {
        var filmController = new FilmController();
        var film1 = new Film(1);
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(Duration.ofMinutes(91));
        var film0 = new Film(10);
        var film1upd1 = new Film(1);
        film1upd1.setName("");
        film1upd1.setDescription("описание1 обновлено1");
        film1upd1.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1upd1.setDuration(Duration.ofMinutes(89));
        var film1upd2 = new Film(1);
        film1upd2.setName("фильм1 обновлен2");
        film1upd2.setDescription("описание1 обновлено2---------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------" +
                "больше 200 символов");
        film1upd2.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1upd2.setDuration(Duration.ofMinutes(89));
        var film1upd3 = new Film(1);
        film1upd3.setName("фильм1 обновлен3");
        film1upd3.setDescription("описание1 обновлено3");
        film1upd3.setReleaseDate(LocalDate.of(1895, 12, 27));
        film1upd3.setDuration(Duration.ofMinutes(89));
        var film1upd4 = new Film(1);
        film1upd4.setName("фильм1 обновлен4");
        film1upd4.setDescription("описание1 обновлено4");
        film1upd4.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1upd4.setDuration(Duration.ofMinutes(-89));

        filmController.addFilm(film1);
        final FilmNotFoundException e0 = assertThrows(FilmNotFoundException.class, () -> filmController.updFilm(film0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd1));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd2));
        final ValidationException e3 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd3));
        final ValidationException e4 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd4));

        assertEquals("Не возможно обновить фильм. Такого фильма не существует.", e0.getMessage());
        assertEquals("Название фильма не может быть пустым.", e1.getMessage());
        assertEquals("Максимальная длина описания фильма — 200 символов.", e2.getMessage());
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", e3.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", e4.getMessage());

    }

    @Test
    void checkGetFilms() {
        var filmController = new FilmController();
        var film1 = new Film(1);
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(Duration.ofMinutes(91));
        var film2 = new Film(2);
        film2.setName("фильм2");
        film2.setDescription("описание2");
        film2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2.setDuration(Duration.ofMinutes(92));

        filmController.addFilm(film1);
        filmController.addFilm(film2);
        var getAll = filmController.getFilms();

        assertNotNull(getAll, "Метод вернул null");
        assertFalse(getAll.isEmpty(), "Список фильмов пуст");
        assertEquals(2, getAll.size(), "Неверное количество фильмов.");
        assertEquals(film1, getAll.get(0), "Фильмы не совпадают.");
        assertEquals(film2, getAll.get(1), "Фильмы не совпадают.");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    void checkAddUser() {
        var userController = new UserController();
        var user1 = new User(1);
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        var user0 = new User(1);
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
        var userController = new UserController();
        var user1 = new User(1);
        user1.setEmail("user1@user.com");
        user1.setLogin("login1");
        user1.setName("username1");
        user1.setBirthday(LocalDate.of(1989, 4, 20));
        var user0 = new User(10);
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
        var userController = new UserController();
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
