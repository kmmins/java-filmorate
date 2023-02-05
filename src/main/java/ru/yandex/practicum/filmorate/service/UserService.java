package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //////////////////////////////////////////////методы userStorage////////////////////////////////////////////////////
    public boolean containsEmail(User user) {
        return userStorage.containsEmail(user);
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public boolean notContainsUser(int id) {
        return userStorage.notContainsUser(id);
    }

    public User updUser(User user) {
        return userStorage.updUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }
    //////////////////////////////////////////////методы userStorage////////////////////////////////////////////////////

    public void addFriend(int id1, int id2) {
        var user1 = userStorage.getUserById(id1);
        var user2 = userStorage.getUserById(id2);

        if (user1.getFriendsSet() == null) {
            user1.setFriendsSet(new HashSet<>());
        }
        if (user2.getFriendsSet() == null) {
            user2.setFriendsSet(new HashSet<>());
        }

        user1.getFriendsSet().add(id2);
        userStorage.updUser(user1);
        user2.getFriendsSet().add(id1);
        userStorage.updUser(user2);
    }

    public void delFriend(int id1, int id2) {
        var user1 = userStorage.getUserById(id1);
        var user2 = userStorage.getUserById(id2);

        user1.getFriendsSet().remove(id2);
        userStorage.updUser(user1);
        user2.getFriendsSet().remove(id1);
        userStorage.updUser(user2);
    }

    public List<User> getFriends(int id) {
        List<User> thisUserFriendsList = new ArrayList<>();
        var thisUser = userStorage.getUserById(id);
        var thisUserFriendsSet = thisUser.getFriendsSet();

        for (int e : thisUserFriendsSet) {
            thisUserFriendsList.add(userStorage.getUserById(e));
        }

        return thisUserFriendsList;
    }

    public List<User> getCommonFriends(int id1, int id2) {
        List<User> commonFriendsList = new ArrayList<>();
        Set<Integer> commonFriendsSet = new HashSet<>();
        var user1 = userStorage.getUserById(id1);
        var user2 = userStorage.getUserById(id2);

        for (int e : user1.getFriendsSet()) {
            if (user1.getFriendsSet().contains(e) && user2.getFriendsSet().contains(e)) {
                commonFriendsSet.add(e);
            }
        }
        commonFriendsSet.forEach(e -> {
            commonFriendsList.add(userStorage.getUserById(e));
        });

        return commonFriendsList;
    }
}
