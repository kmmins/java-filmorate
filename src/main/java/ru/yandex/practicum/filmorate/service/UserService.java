package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AbstractStorage<User> userStorage;

    @Autowired
    public UserService(AbstractStorage<User> userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.add(user);
    }

    public User updUser(User user) {
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(int id) {
        return userStorage.getById(id);
    }

    public void addFriend(int id, int friendId) {
        var userId = userStorage.getById(id);
        var userFriendId = userStorage.getById(friendId);

        userId.getFriendsSet().add(friendId);
        userStorage.update(userId);
        userFriendId.getFriendsSet().add(id);
        userStorage.update(userFriendId);
    }

    public void delFriend(int id, int friendId) {
        var userId = userStorage.getById(id);
        var userFriendId = userStorage.getById(friendId);

        userId.getFriendsSet().remove(friendId);
        userStorage.update(userId);
        userFriendId.getFriendsSet().remove(id);
        userStorage.update(userFriendId);
    }

    public List<User> getFriends(int id) {
        var thisUser = userStorage.getById(id);
        var thisUserFriendsSet = thisUser.getFriendsSet();

        return thisUserFriendsSet
                .stream()
                .map(user -> userStorage.getById(user))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriendsList = new ArrayList<>();
        Set<Integer> commonFriendsSet = new HashSet<>();
        var thisUser = userStorage.getById(id);
        var otherUser = userStorage.getById(otherId);

        for (int e : thisUser.getFriendsSet()) {
            if (thisUser.getFriendsSet().contains(e) && otherUser.getFriendsSet().contains(e)) {
                commonFriendsSet.add(e);
            }
        }
        commonFriendsSet.forEach(element -> {
            commonFriendsList.add(userStorage.getById(element));
        });

        return commonFriendsList;
    }
}
