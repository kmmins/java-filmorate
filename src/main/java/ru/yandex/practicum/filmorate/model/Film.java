package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Film {
    private final int id;
    @NotBlank
    private String name;
    @NotBlank
    @Size (max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
