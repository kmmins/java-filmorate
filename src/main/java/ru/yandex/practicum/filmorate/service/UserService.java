package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AbstractStorage<User> userStorage;

    @Autowired
    public UserService(@Qualifier("dbUserStorage") AbstractStorage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        boolean check = getAllUsers()
                .stream()
                .anyMatch(u -> Objects.equals(u.getEmail(), user.getEmail()));
        if (check) {
            throw new UserAlreadyExistException(String.format("Пользователь с электронной почтой %s уже существует.", user.getEmail()));
        }

        return userStorage.add(user);
    }

    public User updUser(User user) {
        getUserById(user.getId());

        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        var userById = userStorage.getById(id);
        if (userById == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id %d", id));
        }

        return userStorage.getById(id);
    }

    public void addFriend(int id, int friendId) {
        var userId = getUserById(id);
        var userFriendId = getUserById(friendId);

        userId.getFriendsMap().put(userFriendId.getId(), false);
        userStorage.update(userId);
    }

    public void approve(int id, int friendId) {
        var userId = getUserById(id);

        if (userId.getFriendsMap().containsKey(friendId)) {
            userId.getFriendsMap().put(friendId, true);
            userStorage.update(userId);
        }
    }

    public void delFriend(int id, int friendId) {
        var userId = getUserById(id);

        userId.getFriendsMap().remove(friendId);
        userStorage.update(userId);
    }

    public List<User> getFriends(int id) {
        var thisUser = getUserById(id);
        var thisUserFriendsMap = thisUser.getFriendsMap();

        return thisUserFriendsMap.keySet().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriendsList = new ArrayList<>();
        var thisUser = getUserById(id);
        var otherUser = getUserById(otherId);

        Set<Integer> thisUserFriendsSet = new HashSet<>(thisUser.getFriendsMap().keySet());
        Set<Integer> otherUserFriendsSet = new HashSet<>(otherUser.getFriendsMap().keySet());

        Set<Integer> commonFriendsSet = new HashSet<>(thisUserFriendsSet);
        commonFriendsSet.retainAll(otherUserFriendsSet);

        commonFriendsSet.forEach(e -> commonFriendsList.add(getUserById(e)));
        return commonFriendsList;
    }
}
