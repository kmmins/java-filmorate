package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    private final int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
}
