package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class UserController {

    int countUsers = 0;
    Validator validator = new Validator();
    private final HashMap<Integer, User> userBase = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/user")
    public User addUser(@RequestBody User user) {
        if (userBase.containsKey(user.getId())) {
            var e = new UserAlreadyExistException("Не возможно добавить пользователя. Пользователь c таким id" +
                    " уже существует.");
            log.error("При обработке запроса GET /user произошла ошибка: " + e.getMessage());
            throw e;
        } else {
            try {
                validator.validateUser(user);
                countUsers++;
                var addedUser = new User(countUsers);
                addedUser.setEmail(user.getEmail());
                addedUser.setLogin(user.getLogin());
                if (!user.getName().isEmpty()) {
                    addedUser.setName(user.getName());
                } else {
                    addedUser.setName(user.getLogin());
                }
                addedUser.setBirthday(user.getBirthday());

                userBase.put(countUsers, addedUser);
                log.debug("Пользователь добавлен: {}.", addedUser);
                return addedUser;
            } catch (ValidationException e) {
                log.error("При обработке запроса GET /user произошла ошибка валидации: " + e.getMessage());
                throw e;
            }
        }
    }

    @PutMapping("/user")
    public User updUser(@RequestBody User user) {
        if (userBase.containsKey(user.getId())) {
            try {
                validator.validateUser(user);
                userBase.put(user.getId(), user);
                log.debug("Пользователь обновлен: {}.", user);
                return user;
            } catch (ValidationException e) {
                log.error("При обработке запроса PUT /user произошла ошибка: " + e.getMessage());
                throw e;
            }
        } else {
            var e = new UserNotFoundException("Не возможно обновить данные пользователя. Такого пользователя с таким id" +
                    " не существует.");
            log.error("При обработке запроса PUT /film произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        var getAllUsers = new ArrayList<>(userBase.values());
        var size = getAllUsers.size();
        log.debug("Обработка запроса GET /users: Всего пользователей: {}.", size);
        return getAllUsers;
    }
}
