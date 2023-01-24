package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
public class CustomValidator {

    public void validateFilms(Film film) throws CustomValidationException {
         if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
             throw new CustomValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
         }
    }
}
