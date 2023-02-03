package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final CustomValidator validator;
    private final InMemoryFilmStorage inMemoryFilmStorage;

    private final FilmService filmService;

    @Autowired
    public FilmController(CustomValidator validator, InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.validator = validator;
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (CustomValidationException e) {
            log.error("При обработке запроса GET /films произошла ошибка валидации: " + e.getMessage());
            throw e;
        }
        var addedFilm = inMemoryFilmStorage.addFilm(film);
        log.debug("Добавлен фильм: {}.", addedFilm);
        return addedFilm;
    }

    @PutMapping
    public Film updFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (CustomValidationException e) {
            log.error("При обработке запроса PUT /films произошла ошибка валидации: " + e.getMessage());
            throw e;
        }
        if (!inMemoryFilmStorage.containsFilm(film.getId())) {
            var e = new FilmNotFoundException("Не возможно обновить фильм. Фильма с таким id не существует в базе.");
            log.error("При обработке запроса PUT /films произошла ошибка: " + e.getMessage());
            throw e;
        }
        var updatedFilm = inMemoryFilmStorage.updFilm(film);
        log.debug("Фильм обновлен: {}.", updatedFilm);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        var allFilms = inMemoryFilmStorage.getAllFilms();
        var size = allFilms.size();
        log.debug("Обработка запроса GET /films: Текущее количество фильмов: {}", size);
        return allFilms;
    }

    @GetMapping("/filmId")
    public Film getFilmById(@PathVariable int filmId) {
        if (!inMemoryFilmStorage.containsFilm(filmId)) {
            var e = new FilmNotFoundException("Не удалось получить данные фильма. Фильма с таким id не существует в базе.");
            log.error("При обработке запроса GET /films/{filmId} произошла ошибка: " + e.getMessage());
            throw e;
        }
        var filmGetById = inMemoryFilmStorage.getFilmById(filmId);
        log.debug("Получены данные фильма: " + filmGetById);
        return filmGetById;
    }
}
