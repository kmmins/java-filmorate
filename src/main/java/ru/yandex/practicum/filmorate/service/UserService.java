package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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

        userId.getFriendsMap().put(userFriendId.getId(), false);
        userStorage.update(userId);
    }

    public void approve(int id, int friendId) {
        var userId = userStorage.getById(id);

        if (userId.getFriendsMap().containsKey(friendId)) {
            userId.getFriendsMap().put(friendId, true);
            userStorage.update(userId);
        }
    }

    public void delFriend(int id, int friendId) {
        var userId = userStorage.getById(id);

        userId.getFriendsMap().remove(friendId);
        userStorage.update(userId);
    }

    public List<User> getFriends(int id) {
        var thisUser = userStorage.getById(id);
        var thisUserFriendsMap = thisUser.getFriendsMap();

        return thisUserFriendsMap.keySet().stream()
                //.filter(e -> e.getValue().equals(true))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriendsList = new ArrayList<>();
        var thisUser = userStorage.getById(id);
        var otherUser = userStorage.getById(otherId);

        //.filter(e -> e.getValue().equals(true))
        Set<Integer> thisUserFriendsSet = new HashSet<>(thisUser.getFriendsMap().keySet());
        //.filter(e -> e.getValue().equals(true))
        Set<Integer> otherUserFriendsSet = new HashSet<>(otherUser.getFriendsMap().keySet());

        Set<Integer> commonFriendsSet = new HashSet<>(thisUserFriendsSet);
        commonFriendsSet.retainAll(otherUserFriendsSet);

        commonFriendsSet.forEach(e -> commonFriendsList.add(userStorage.getById(e)));
        return commonFriendsList;
    }
}
