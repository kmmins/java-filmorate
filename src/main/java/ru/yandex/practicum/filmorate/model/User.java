package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    private final int id;
    @Email(message = "должно иметь формат адреса электронной почты")
    private String email;
    @NotBlank(message = "не должно быть пустым")
    private String login;
    private String name;
    @Past(message = "должно содержать прошедшую дату")
    private LocalDate birthday;
    // переработать структуру хранения данных о друзьях пользовательей вероятно в hashmap
    private HashMap<Integer, FriendStatus> friendsMap;

    public String getName() {
        if (name == null || name.isEmpty()) {
            return login;
        } else {
            return name;
        }
    }

    public HashMap<Integer, FriendStatus> getFriendsMap() {
        if (friendsMap == null) {
            friendsMap = new HashMap<>();
        }
        return friendsMap;
    }
}