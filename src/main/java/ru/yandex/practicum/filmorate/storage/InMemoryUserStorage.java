package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int countUsers = 0;
    private final HashMap<Integer, User> userBase = new HashMap<>();

    @Override
    public boolean containsEmail(User user) {
        var checkedEmail = user.getEmail();
        var allUsers = getAllUsers();

        for (User element : allUsers) {
            if (element.getEmail().equals(checkedEmail)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public User addUser(User user) {
        if (containsEmail(user)) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой: " + user.getEmail() + " уже существует.");
        }
        countUsers++;
        var createdUser = new User(countUsers, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getFriendsSet());
        userBase.put(countUsers, createdUser);
        return createdUser;
    }

    @Override
    public boolean notContainsUser(int id) {
        return !userBase.containsKey(id);
    }

    @Override
    public User updUser(User user) {
        if (notContainsUser(user.getId())) {
            throw new UserNotFoundException("Не возможно обновить данные пользователя. Не найден пользователь с id: " + user.getId());
        }
        userBase.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userBase.values());
    }

    @Override
    public User getUserById(int id) {
        var result = userBase.get(id);
        if (result == null) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        return result;
    }
}
