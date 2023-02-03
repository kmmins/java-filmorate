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

    public void addFriend(int id1, int id2) {
        var user1 = userStorage.getUserById(id1);
        var user2 = userStorage.getUserById(id2);

        user1.getFriendsSet().add(id2);
        user2.getFriendsSet().add(id1);
    }

    public void delFriend(int id1, int id2) {
        var user1 = userStorage.getUserById(id1);
        var user2 = userStorage.getUserById(id2);

        user1.getFriendsSet().remove(id2);
        user2.getFriendsSet().remove(id1);
    }

    public List<User> getFriends(int id) {
        List<User> thisUserFriendsLists = new ArrayList<>();
        var allUsers = userStorage.getAllUsers();
        var thisUser = userStorage.getUserById(id);
        var thisUserFriendsSet = thisUser.getFriendsSet();

        for (int i = 0; i < thisUserFriendsSet.size(); i++) {
            thisUserFriendsLists.add(allUsers.get(i));
        }
        return thisUserFriendsLists;
    }

    public List<User> getCommonFriends(int id1, int id2) {
        List<User> commonFriendsList = new ArrayList<>();
        var user1 = userStorage.getUserById(id1);
        var user2 = userStorage.getUserById(id2);
        List<User> allUsers = userStorage.getAllUsers();

        Set<Integer> commonFriendsSet = new HashSet<>();
        for (int i = 0; i < user1.getFriendsSet().size(); i++) {
            if (user1.getFriendsSet().contains(i) && user2.getFriendsSet().contains(i)) {
                commonFriendsSet.add(i);
            }
        }
        for (int i = 0; i < commonFriendsSet.size(); i++) {
            commonFriendsList.add(allUsers.get(i));
        }
        return commonFriendsList;
    }
}
