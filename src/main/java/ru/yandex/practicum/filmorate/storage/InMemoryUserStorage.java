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
     * @param user для проверки на содержание в базе
     * @return булево значение для пользователя на содержание в базе
     */
    public boolean containsUser(User user) {
        return userBase.containsKey(user.getId());
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
}
