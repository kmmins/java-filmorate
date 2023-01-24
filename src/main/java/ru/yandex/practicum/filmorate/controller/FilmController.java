package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validator;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    protected int countFilms = 0;
    public Validator validator = new Validator();
    private final HashMap<Integer, Film> filmsDatabase = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (ValidationException e) {
            log.error("При обработке запроса GET /film произошла ошибка валидации: " + e.getMessage());
            throw e;
        }
        if (filmsDatabase.containsKey(film.getId())) {
            var e = new FilmAlreadyExistException("Не возможно добавить фильм. Фильм с таким id уже существует.");
            log.error("При обработке запроса GET /film произошла ошибка: " + e.getMessage());
            throw e;
        }
        countFilms++;
        var addedFilm = new Film(countFilms, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration());
        filmsDatabase.put(countFilms, addedFilm);
        log.debug("Добавлен фильм: {}.", addedFilm);
        return addedFilm;
    }

    @PutMapping
    public Film updFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (ValidationException e) {
            log.error("При обработке запроса PUT /film произошла ошибка валидации: " + e.getMessage());
            throw e;
        }
        if (!filmsDatabase.containsKey(film.getId())) {
            var e = new FilmNotFoundException("Не возможно обновить фильм. Такого фильма не существует.");
            log.error("При обработке запроса PUT /film произошла ошибка: " + e.getMessage());
            throw e;
        }
        filmsDatabase.put(film.getId(), film);
        log.debug("Фильм обновлен: {}.", film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        var getAllFilms = new ArrayList<>(filmsDatabase.values());
        var size = getAllFilms.size();
        log.debug("Обработка запроса GET /films: Текущее количество фильмов: {}", size);
        return getAllFilms;
    }
}
