package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DbFilmStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFilmStorageTest {

    private final DbFilmStorage filmStorage;

    @Test
    public void checkAddFilm() {
        var film1 = new Film();
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2000, 1, 1));
        Set<Genre> film1Genre = new HashSet<>();
        film1Genre.add(new Genre(1));
        film1.setGenres(film1Genre);
        film1.setMpa(new Mpa(1));
        film1.setDuration(90);
    }

    @Test
    public void checkGetAllFilms() {
    }

    @Test
    public void checkGetByIdFilm() {
        var film1 = new Film();
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setMpa(new Mpa(1));
        film1.setDuration(91);
        filmStorage.add(film1);

        Optional<Film> userOptional = Optional.ofNullable(filmStorage.getById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void checkUpdateFilm() {
    }
}
