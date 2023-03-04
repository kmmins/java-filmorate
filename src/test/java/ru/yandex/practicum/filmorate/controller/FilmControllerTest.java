package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {

    private final FilmController filmController;

    @Autowired
    public FilmControllerTest(FilmController filmController) {
        this.filmController = filmController;
    }

    @Test
    void checkAddFilm() {
        var film1 = new Film();
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setMpa(1);
        film1.setDuration(91);
        filmController.addFilm(film1);
        var film2 = new Film();
        film2.setName("");
        film2.setDescription("описание2");
        film2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2.setMpa(1);
        film2.setDuration(92);
        var film3 = new Film();
        film3.setName("фильм3");
        film3.setDescription("описание3------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------" +
                "больше 200 символов");
        film3.setReleaseDate(LocalDate.of(2023, 1, 3));
        film3.setMpa(1);
        film3.setDuration(93);
        var film4 = new Film();
        film4.setName("фильм4");
        film4.setDescription("описание4");
        film4.setReleaseDate(LocalDate.of(1895, 12, 27));
        film4.setMpa(1);
        film4.setDuration(94);
        var film5 = new Film();
        film5.setName("фильм5");
        film5.setDescription("описание5");
        film5.setReleaseDate(LocalDate.of(2023, 1, 5));
        film5.setMpa(1);
        film5.setDuration(-95);

        final ValidationException e1 = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> filmController.addFilm(film3));
        final CustomValidationException e3 = assertThrows(CustomValidationException.class,
                () -> filmController.addFilm(film4));
        final ValidationException e4 = assertThrows(ValidationException.class, () -> filmController.addFilm(film5));

        assertEquals("addFilm.film.name: не должно быть пустым", e1.getMessage());
        assertEquals("addFilm.film.description: размер должен находиться в диапазоне от 0 до 200", e2.getMessage());
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", e3.getMessage());
        assertEquals("addFilm.film.duration: должно быть больше 0", e4.getMessage());
    }

    @Test
    void checkUpdFilm() {
        var film2 = new Film();
        film2.setName("фильм2");
        film2.setDescription("описание2");
        film2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2.setMpa(1);
        film2.setDuration(92);
        var addedFilm2 = filmController.addFilm(film2);
        var film0 = new Film(100500);
        film0.setName("фильм0");
        film0.setDescription("описание0");
        film0.setReleaseDate(LocalDate.of(2023, 1, 2));
        film0.setMpa(1);
        film0.setDuration(92);
        var film2upd1 = new Film(addedFilm2.getId());
        film2upd1.setName("");
        film2upd1.setDescription("описание2 обновлено1");
        film2upd1.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2upd1.setMpa(1);
        film2upd1.setDuration(92);
        var film2upd2 = new Film(addedFilm2.getId());
        film2upd2.setName("фильм2 обновлен2");
        film2upd2.setDescription("описание2 обновлено2---------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------" +
                "больше 200 символов");
        film2upd2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2upd2.setMpa(1);
        film2upd2.setDuration(92);
        var film2upd3 = new Film(addedFilm2.getId());
        film2upd3.setName("фильм1 обновлен3");
        film2upd3.setDescription("описание1 обновлено3");
        film2upd3.setReleaseDate(LocalDate.of(1895, 12, 27));
        film2upd3.setMpa(1);
        film2upd3.setDuration(92);
        var film2upd4 = new Film(addedFilm2.getId());
        film2upd4.setName("фильм1 обновлен4");
        film2upd4.setDescription("описание1 обновлено4");
        film2upd4.setReleaseDate(LocalDate.of(2022, 1, 1));
        film2upd4.setMpa(1);
        film2upd4.setDuration(-92);

        final FilmNotFoundException e0 = assertThrows(FilmNotFoundException.class, () -> filmController.updFilm(film0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> filmController.updFilm(film2upd1));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> filmController.updFilm(film2upd2));
        final CustomValidationException e3 = assertThrows(CustomValidationException.class,
                () -> filmController.updFilm(film2upd3));
        final ValidationException e4 = assertThrows(ValidationException.class, () -> filmController.updFilm(film2upd4));

        assertEquals("Не возможно обновить фильм. Не найден фильм c id: 100500", e0.getMessage());
        assertEquals("updFilm.film.name: не должно быть пустым", e1.getMessage());
        assertEquals("updFilm.film.description: размер должен находиться в диапазоне от 0 до 200", e2.getMessage());
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", e3.getMessage());
        assertEquals("updFilm.film.duration: должно быть больше 0", e4.getMessage());
    }

    @Test
    void checkGetFilms() {
        var getAllBefore = filmController.getAllFilms();
        int sizeBefore = getAllBefore.size();
        var film3 = new Film();
        film3.setName("фильм3");
        film3.setDescription("описание3");
        film3.setReleaseDate(LocalDate.of(2023, 1, 3));
        film3.setMpa(1);
        film3.setDuration(93);
        var addedFilm3 = filmController.addFilm(film3);
        var film4 = new Film();
        film4.setName("фильм4");
        film4.setDescription("описание4");
        film4.setReleaseDate(LocalDate.of(2023, 1, 4));
        film4.setMpa(1);
        film4.setDuration(94);
        var addedFilm4 = filmController.addFilm(film4);

        var getAllAfter = filmController.getAllFilms();
        int sizeAfter = getAllAfter.size();

        assertNotNull(getAllAfter, "Метод вернул null");
        assertFalse(getAllAfter.isEmpty(), "Список фильмов пуст");
        assertEquals(sizeBefore + 2, sizeAfter, "Неверное количество фильмов.");
        assertEquals(addedFilm3, getAllAfter.get(sizeBefore), "Фильмы не совпадают.");
        assertEquals(addedFilm4, getAllAfter.get(sizeBefore + 1), "Фильмы не совпадают.");
    }
}
