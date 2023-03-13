package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements AbstractStorage<User> {

    private int countUsers = 0;
    private final HashMap<Integer, User> userBase = new HashMap<>();

    private boolean containsEmail(User user) {
        var checkedEmail = user.getEmail();
        var allUsers = getAll();

        for (User element : allUsers) {
            if (element.getEmail().equals(checkedEmail)) {
                return true;
            }
        }
        return false;
    }

    private boolean notContainsUser(User user) {
        return !userBase.containsKey(user.getId());
    }

    @Override
    public User add(User user) {
        if (containsEmail(user)) {
            throw new UserAlreadyExistException(String.format("Пользователь с электронной почтой %s уже существует.", user.getEmail()));
        }
        countUsers++;
        var createdUser = new User(
                countUsers,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriendsMap());
        userBase.put(countUsers, createdUser);
        return createdUser;
    }

    @Override
    public User update(User user) {
        if (notContainsUser(user)) {
            throw new UserNotFoundException(String.format("Не возможно обновить данные. Не найден пользователь с id %d.", user.getId()));
        }
        userBase.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userBase.values());
    }

    @Override
    public User getById(int id) {
        var result = userBase.get(id);
        if (result == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id %d.", id));
        }
        return result;
    }
}
