package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        var addedUser = userService.addUser(user);
        log.debug("Обработка запроса POST /users. Пользователь добавлен: {}.", addedUser);
        return addedUser;
    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        var updatedUser = userService.updUser(user);
        log.debug("Обработка запроса PUT /users. Пользователь обновлен: {}.", updatedUser);
        return updatedUser;
    }

    @GetMapping
    public List<User> getAllUsers() {
        var allUsers = userService.getAllUsers();
        var size = allUsers.size();
        log.debug("Обработка запроса GET /users. Всего пользователей: {}.", size);
        return allUsers;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        var userGetById = userService.getUserById(id);
        log.debug("Обработка запроса GET /users/{id}. Получены данные пользователя: {}.", userGetById);
        return userGetById;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void weAreFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (friendId == null) {
            throw new IncorrectParameterException("Параметр friendId равен null.");
        }
        userService.addFriend(id, friendId);
        log.debug("Обработка запроса PUT /users/{id}/friends/{friendId}. Обновлены данные пользователей с id: {}, {}.", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void noLongerFriends(@PathVariable Integer id, @PathVariable Integer friendId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (friendId == null) {
            throw new IncorrectParameterException("Параметр friendId равен null.");
        }
        userService.delFriend(id, friendId);
        log.debug("Обработка запроса DELETE /users/{id}/friends/{friendId}. Обновлены данные пользователей с id: {}, {}.", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        var userFriendsList = userService.getFriends(id);
        log.debug("Обработка запроса GET /users/{id}/friends. Получены данные пользователя с id: {}.", id);
        return userFriendsList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (otherId == null) {
            throw new IncorrectParameterException("Параметр otherId равен null.");
        }
        var usersCommonFriendsList = userService.getCommonFriends(id, otherId);
        log.debug("Обработка запроса GET /users/{id}/friends/common/{otherId}. Получены данные пользователя с id: {}.", id);
        return usersCommonFriendsList;
    }
}
