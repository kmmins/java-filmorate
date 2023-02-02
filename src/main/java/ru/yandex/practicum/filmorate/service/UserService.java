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

    public void addFriend(User user1, User user2) {
        user1.getFriendsSet().add(user2.getId());
        user2.getFriendsSet().add(user1.getId());
    }

    public void delFriend(User user1, User user2) {
        user1.getFriendsSet().remove(user2.getId());
        user2.getFriendsSet().remove(user1.getId());
    }

    public Set<Integer> getFriends(User user) {
        return user.getFriendsSet();
    }

    public List<User> getCollectiveFriends(User user1, User user2) {
        List<User> collectiveFriendsList = new ArrayList<>();
        List<User> allUsers = userStorage.getAllUsers();

        Set<Integer> collectiveFriends = new HashSet<>();
        for (int i = 0; i < user1.getFriendsSet().size(); i++) {
            if (user1.getFriendsSet().contains(i) && user2.getFriendsSet().contains(i)) {
                collectiveFriends.add(i);
            }
        }
        for (int i = 0; i < collectiveFriends.size(); i++) {
            collectiveFriendsList.add(allUsers.get(i));
        }

        return collectiveFriendsList;
    }
}
