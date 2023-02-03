package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private int countUsers = 0;
    private final HashMap<Integer, User> userBase = new HashMap<>();

    @Override
    public User addUser(User user) {
        countUsers++;
        var createdUser = new User(countUsers, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getFriendsSet());
        userBase.put(countUsers, createdUser);
        return createdUser;
    }

    /**
     * Вспомогательный метод
     * @param id пользователя для проверки на содержание его в базе
     * @return булево значение для пользователя на содержание его в базе
     */
    public boolean containsUser(int id) {
        return userBase.containsKey(id);
    }

    @Override
    public User updUser(User user) {
        userBase.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userBase.values());
    }

    @Override
    public User getUserById(int id) {
        return userBase.get(id);
    }
}
