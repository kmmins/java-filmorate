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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFilmStorageTest {

    private final DbFilmStorage filmStorage;

    @Test
    public void checkAddFilm() {
        var film1Db = new Film();
        film1Db.setName("film1Db");
        film1Db.setDescription("description1Db");
        film1Db.setReleaseDate(LocalDate.of(2000, 1, 1));
        Set<Genre> film1DbGenre = new HashSet<>();
        film1DbGenre.add(new Genre(1));
        film1DbGenre.add(new Genre(2));
        film1Db.setGenres(film1DbGenre);
        film1Db.setMpa(new Mpa(3));
        film1Db.setDuration(90);
        var checkSize = filmStorage.getAll().size();

        var addedFilm1Db = filmStorage.add(film1Db);

        assertEquals(checkSize + 1, addedFilm1Db.getId(), "Некорректный id.");
        assertEquals("PG-13", addedFilm1Db.getMpa().getName(), "Некорректный рейтинг MPA.");
    }

    @Test
    public void checkUpdateFilm() {
        var film2Db = new Film();
        film2Db.setName("film2Db");
        film2Db.setDescription("description2Db");
        film2Db.setReleaseDate(LocalDate.of(2000, 1, 2));
        Set<Genre> film2DbGenre = new HashSet<>();
        film2DbGenre.add(new Genre(2));
        film2DbGenre.add(new Genre(6));
        film2Db.setGenres(film2DbGenre);
        film2Db.setMpa(new Mpa(4));
        film2Db.setDuration(90);
        var addedFilm2Db = filmStorage.add(film2Db);
        var likesBefore = addedFilm2Db.getLikesSet().size();
        addedFilm2Db.getGenres().add(new Genre(3));
        addedFilm2Db.getLikesSet().add(1);

        var film2AfterUpdDb = filmStorage.update(addedFilm2Db);

        assertEquals(3, film2AfterUpdDb.getGenres().size(), "Количество жанров не изменилось.");
        assertEquals(likesBefore + 1, film2AfterUpdDb.getLikesSet().size(), "Количество лайков не изменилось.");
    }

    @Test
    public void checkGetAllFilms() {
        var film3Db = new Film();
        film3Db.setName("film3Db");
        film3Db.setDescription("description3Db");
        film3Db.setReleaseDate(LocalDate.of(2000, 1, 3));
        Set<Genre> film3DbGenre = new HashSet<>();
        film3DbGenre.add(new Genre(1));
        film3Db.setGenres(film3DbGenre);
        film3Db.setMpa(new Mpa(1));
        film3Db.setDuration(90);
        var film4Db = new Film();
        film4Db.setName("film4Db");
        film4Db.setDescription("description4Db");
        film4Db.setReleaseDate(LocalDate.of(2000, 1, 4));
        Set<Genre> film4DbGenre = new HashSet<>();
        film4DbGenre.add(new Genre(1));
        film4Db.setGenres(film4DbGenre);
        film4Db.setMpa(new Mpa(1));
        film4Db.setDuration(90);
        var film5Db = new Film();
        film5Db.setName("film5Db");
        film5Db.setDescription("description5Db");
        film5Db.setReleaseDate(LocalDate.of(2000, 1, 5));
        Set<Genre> film5DbGenre = new HashSet<>();
        film5DbGenre.add(new Genre(1));
        film5Db.setGenres(film5DbGenre);
        film5Db.setMpa(new Mpa(1));
        film5Db.setDuration(90);
        var checkSize = filmStorage.getAll().size();
        filmStorage.add(film3Db);
        filmStorage.add(film4Db);
        filmStorage.add(film5Db);

        var checkResult = filmStorage.getAll();

        assertEquals(checkSize + 3, checkResult.size(), "Некорректное количество фильмов.");
    }

    @Test
    public void checkGetByIdFilm() {
        var film6Db = new Film();
        film6Db.setName("фильм1");
        film6Db.setDescription("описание1");
        film6Db.setReleaseDate(LocalDate.of(2023, 1, 1));
        Set<Genre> film6DbGenre = new HashSet<>();
        film6DbGenre.add(new Genre(1));
        film6Db.setGenres(film6DbGenre);
        film6Db.setMpa(new Mpa(1));
        film6Db.setDuration(91);
        var checkSize = filmStorage.getAll().size();
        var addedFilm6Db = filmStorage.add(film6Db);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getById(addedFilm6Db.getId()));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", checkSize + 1)
                                .hasFieldOrPropertyWithValue("name", film.getName())
                                .hasFieldOrPropertyWithValue("description", film.getDescription())
                                .hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate())
                                .hasFieldOrPropertyWithValue("genres", film.getGenres())
                                .hasFieldOrPropertyWithValue("mpa", film.getMpa())
                                .hasFieldOrPropertyWithValue("duration", film.getDuration())
                                .hasFieldOrPropertyWithValue("likesSet", film.getLikesSet())
                );
    }
}
