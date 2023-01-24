package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    protected int countUsers = 0;
    public Validator validator = new Validator();
    private final HashMap<Integer, User> userBase = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        try {
            validator.validateUser(user);
        } catch (ValidationException e) {
            log.error("При обработке запроса GET /user произошла ошибка валидации: " + e.getMessage());
            throw e;
        }
        if (userBase.containsKey(user.getId())) {
            var e = new UserAlreadyExistException("Не возможно добавить пользователя. Пользователь c таким id уже существует.");
            log.error("При обработке запроса GET /user произошла ошибка: " + e.getMessage());
            throw e;
        }
        countUsers++;
        var addedUser = new User(countUsers, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        userBase.put(countUsers, addedUser);
        log.debug("Пользователь добавлен: {}.", addedUser);
        return addedUser;

    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        try {
            validator.validateUser(user);
        } catch (ValidationException e) {
            log.error("При обработке запроса PUT /user произошла ошибка: " + e.getMessage());
            throw e;
        }
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
