package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        var addedUser = inMemoryUserStorage.addUser(user);
        log.debug("(Обработка запроса POST /users). Пользователь добавлен: {}.", addedUser);
        return addedUser;
    }

    @PutMapping
    public User updUser(@Valid @RequestBody User user) {
        if (!inMemoryUserStorage.containsUser(user.getId())) {
            var e = new UserNotFoundException("Не возможно обновить данные пользователя. Пользователя с таким id не существует в базе.");
            log.error("При обработке запроса PUT /users произошла ошибка: {},{}.", e.getMessage(), user.getId());
            throw e;
        }
        var updatedUser = inMemoryUserStorage.updUser(user);
        log.debug("(Обработка запроса PUT /users). Пользователь обновлен: {}.", updatedUser);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        var allUsers = inMemoryUserStorage.getAllUsers();
        var size = allUsers.size();
        log.debug("(Обработка запроса GET /users). Всего пользователей: {}.", size);
        return allUsers;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        if (!inMemoryUserStorage.containsUser(userId)) {
            var e = new UserNotFoundException("Не удалось получить данные пользователя. Пользователя с таким id не существует в базе.");
            log.error("При обработке запроса GET /users/{userId} произошла ошибка: {}, {}.", e.getMessage(), userId);
            throw e;
        }
        var userGetById = inMemoryUserStorage.getUserById(userId);
        log.debug("Получены данные пользователя: {}.", userGetById);
        return userGetById;
    }

    @PutMapping("{userId}/friends/{friendId}")
    public void weAreFriends(@PathVariable int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
        log.debug("(Обработка запроса PUT /users/{userId}/friends/{friendId}). Обновлены данные пользователей с id: {}, {}.", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void noLongerFriends(@PathVariable int userId, @PathVariable int friendId) {
        userService.delFriend(userId, friendId);
        log.debug("(Обработка запроса DELETE /users/{userId}/friends/{friendId}). Обновлены данные пользователей с id: {}, {}.", userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable int userId) {
        var userFriendsList = userService.getFriends(userId);
        log.debug("(Обработка запроса GET /users/{userId}/friends). Получены данные пользователя с id: {}.", userId);
        return userFriendsList;
    }

    @GetMapping
    public List<User> getCommonFriends (@PathVariable int userId, @PathVariable int otherId){
        var usersCommonFriendsList = userService.getCommonFriends(userId, otherId);
        log.debug("(GET /users/{userId}/friends/common/{otherId}. Получены данные пользователя с id: {}.", userId);
        return usersCommonFriendsList;
    }
}
