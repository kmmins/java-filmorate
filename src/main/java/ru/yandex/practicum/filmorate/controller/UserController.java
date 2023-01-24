package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private int countUsers = 0;
    private final HashMap<Integer, User> userBase = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        countUsers++;
        var addedUser = new User(countUsers, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        userBase.put(countUsers, addedUser);
        log.debug("Пользователь добавлен: {}.", addedUser);
        return addedUser;
    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        if (!userBase.containsKey(user.getId())) {
            var e = new UserNotFoundException("Не возможно обновить данные пользователя. Такого пользователя с таким id не существует.");
            log.error("При обработке запроса PUT /film произошла ошибка: " + e.getMessage());
            throw e;
        }
        userBase.put(user.getId(), user);
        log.debug("Пользователь обновлен: {}.", user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        var getAllUsers = new ArrayList<>(userBase.values());
        var size = getAllUsers.size();
        log.debug("Обработка запроса GET /users: Всего пользователей: {}.", size);
        return getAllUsers;
    }
}
