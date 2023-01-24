package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    private final int id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
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