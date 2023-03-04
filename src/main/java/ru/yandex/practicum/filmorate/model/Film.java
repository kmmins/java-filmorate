package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Film {
    private final int id;
    @NotBlank(message = "не должно быть пустым")
    private String name;
    @NotBlank(message = "не должно быть пустым")
    @Size(max = 200, message = "размер должен находиться в диапазоне от 0 до 200")
    private String description;
    private LocalDate releaseDate;
    private Set<Integer> genre;
    private Integer mpa;
    @Positive(message = "должно быть больше 0")
    private int duration;
    private Set<Integer> likesSet;

    public Set<Integer> getGenre() {
        if (genre == null) {
            genre = new HashSet<>();
        }
        return genre;
    }
    public Set<Integer> getLikesSet() {
        if (likesSet == null) {
            likesSet = new HashSet<>();
        }
        return likesSet;
    }
}
