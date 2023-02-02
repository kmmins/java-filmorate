package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        var addedUser = inMemoryUserStorage.addUser(user);
        log.debug("Пользователь добавлен: {}.", addedUser);
        return addedUser;
    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        if (!inMemoryUserStorage.containsUser(user)) {
            var e = new UserNotFoundException("Не возможно обновить данные пользователя. Пользователя с таким id не существует в базе.");
            log.error("При обработке запроса PUT /film произошла ошибка: " + e.getMessage());
            throw e;
        }
        var updatedUser = inMemoryUserStorage.updUser(user);
        log.debug("Пользователь обновлен: {}.", updatedUser);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        var allUsers = inMemoryUserStorage.getAllUsers();
        var size = allUsers.size();
        log.debug("Обработка запроса GET /users: Всего пользователей: {}.", size);
        return allUsers;
    }
}
