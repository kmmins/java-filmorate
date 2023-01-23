package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    private final int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    public String getName() {
        if (name == null || name.isEmpty()) {
            return login;
        }
        else {
            return name;
        }
    }
}