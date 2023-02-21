package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FriendStatus;
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

        userId.getFriendsMap().put(friendId, FriendStatus.UNCONFIRMED);
        userStorage.update(userId);
        userFriendId.getFriendsMap().put(id, FriendStatus.UNCONFIRMED);
        userStorage.update(userFriendId);
    }

    public void delFriend(int id, int friendId) {
        var userId = userStorage.getById(id);
        var userFriendId = userStorage.getById(friendId);

        userId.getFriendsMap().remove(friendId);
        userStorage.update(userId);
        userFriendId.getFriendsMap().remove(id);
        userStorage.update(userFriendId);
    }

    public List<User> getFriends(int id) {
        var thisUser = userStorage.getById(id);
        var thisUserFriendsMap = thisUser.getFriendsMap();

        return thisUserFriendsMap.entrySet().stream()
                .filter(e -> e.getValue().equals(FriendStatus.CONFIRMED))
                .map(e -> userStorage.getById(e.getKey()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriendsList = new ArrayList<>();
        var thisUser = userStorage.getById(id);
        var otherUser = userStorage.getById(otherId);

        Set<Integer> thisUserFriendsSet = thisUser.getFriendsMap().entrySet().stream()
                .filter(e -> e.getValue().equals(FriendStatus.CONFIRMED))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Set<Integer> otherUserFriendsSet = otherUser.getFriendsMap().entrySet().stream()
                .filter(e -> e.getValue().equals(FriendStatus.CONFIRMED))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        Set<Integer> commonFriendsSet = new HashSet<>(thisUserFriendsSet);
        commonFriendsSet.retainAll(otherUserFriendsSet);

        commonFriendsSet.forEach(e -> commonFriendsList.add(userStorage.getById(e)));
        return commonFriendsList;
    }
}
