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

    @Autowired
    private FilmController filmController;

    @Test
    void checkAddFilm() {
        var film1 = new Film();
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(91);
        filmController.addFilm(film1);
        var film2 = new Film();
        film2.setName("");
        film2.setDescription("описание2");
        film2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2.setDuration(92);
        var film3 = new Film();
        film3.setName("фильм3");
        film3.setDescription("описание3------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------" +
                "больше 200 символов");
        film3.setReleaseDate(LocalDate.of(2023, 1, 3));
        film3.setDuration(93);
        var film4 = new Film();
        film4.setName("фильм4");
        film4.setDescription("описание4");
        film4.setReleaseDate(LocalDate.of(1895, 12, 27));
        film4.setDuration(94);
        var film5 = new Film();
        film5.setName("фильм5");
        film5.setDescription("описание5");
        film5.setReleaseDate(LocalDate.of(2023, 1, 5));
        film5.setDuration(-95);

        final ValidationException e1 = assertThrows(ValidationException.class, () -> filmController.addFilm(film2));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> filmController.addFilm(film3));
        final CustomValidationException e3 = assertThrows(CustomValidationException.class,
                () -> filmController.addFilm(film4));
        final ValidationException e4 = assertThrows(ValidationException.class, () -> filmController.addFilm(film5));

        assertEquals("Название фильма не может быть пустым.", e1.getMessage());
        assertEquals("Максимальная длина описания фильма — 200 символов.", e2.getMessage());
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", e3.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", e4.getMessage());
    }

    @Test
    void checkUpdFilm() {
        var film1 = new Film();
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(91);
        var addedFilm1 = filmController.addFilm(film1);
        var film0 = new Film(100500);
        film0.setName("фильм0");
        film0.setDescription("описание0");
        film0.setReleaseDate(LocalDate.of(2023, 1, 1));
        film0.setDuration(91);
        var film1upd1 = new Film(addedFilm1.getId());
        film1upd1.setName("");
        film1upd1.setDescription("описание1 обновлено1");
        film1upd1.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1upd1.setDuration(89);
        var film1upd2 = new Film(addedFilm1.getId());
        film1upd2.setName("фильм1 обновлен2");
        film1upd2.setDescription("описание1 обновлено2---------------------------------------------------------------" +
                "---------------------------------------------------------------------------------------------------" +
                "больше 200 символов");
        film1upd2.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1upd2.setDuration(89);
        var film1upd3 = new Film(addedFilm1.getId());
        film1upd3.setName("фильм1 обновлен3");
        film1upd3.setDescription("описание1 обновлено3");
        film1upd3.setReleaseDate(LocalDate.of(1895, 12, 27));
        film1upd3.setDuration(89);
        var film1upd4 = new Film(addedFilm1.getId());
        film1upd4.setName("фильм1 обновлен4");
        film1upd4.setDescription("описание1 обновлено4");
        film1upd4.setReleaseDate(LocalDate.of(2022, 1, 1));
        film1upd4.setDuration(-89);

        final FilmNotFoundException e0 = assertThrows(FilmNotFoundException.class, () -> filmController.updFilm(film0));
        final ValidationException e1 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd1));
        final ValidationException e2 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd2));
        final CustomValidationException e3 = assertThrows(CustomValidationException.class,
                () -> filmController.updFilm(film1upd3));
        final ValidationException e4 = assertThrows(ValidationException.class, () -> filmController.updFilm(film1upd4));

        assertEquals("Не возможно обновить фильм. Такого фильма не существует.", e0.getMessage());
        assertEquals("Название фильма не может быть пустым.", e1.getMessage());
        assertEquals("Максимальная длина описания фильма — 200 символов.", e2.getMessage());
        assertEquals("Дата релиза фильма не может быть раньше 28 декабря 1895 года.", e3.getMessage());
        assertEquals("Продолжительность фильма должна быть положительной.", e4.getMessage());
    }

    @Test
    void checkGetFilms() {
        var getAll = filmController.getFilms();
        int sizeBefore = getAll.size();
        var film1 = new Film();
        film1.setName("фильм1");
        film1.setDescription("описание1");
        film1.setReleaseDate(LocalDate.of(2023, 1, 1));
        film1.setDuration(91);
        filmController.addFilm(film1);
        var film2 = new Film();
        film2.setName("фильм2");
        film2.setDescription("описание2");
        film2.setReleaseDate(LocalDate.of(2023, 1, 2));
        film2.setDuration(92);
        filmController.addFilm(film2);

        getAll = filmController.getFilms();

        assertNotNull(getAll, "Метод вернул null");
        assertFalse(getAll.isEmpty(), "Список фильмов пуст");
        assertEquals(sizeBefore + 2, getAll.size(), "Неверное количество фильмов.");
        assertEquals(film1, getAll.get(sizeBefore), "Фильмы не совпадают.");
        assertEquals(film2, getAll.get(sizeBefore + 1), "Фильмы не совпадают.");
    }
}
