package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {

    private final CustomValidator validator;
    private final FilmService filmService;

    @Autowired
    public FilmController(CustomValidator validator, FilmService filmService) {
        this.validator = validator;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (CustomValidationException e) {
            log.error("При обработке запроса GET /films произошла ошибка валидации: {}.", e.getMessage());
            throw e;
        }
        var addedFilm = filmService.addFilm(film);
        log.debug("Обработка запроса POST /films. Добавлен фильм: {}.", addedFilm);
        return addedFilm;
    }

    @PutMapping
    public Film updFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (CustomValidationException e) {
            log.error("При обработке запроса PUT /films произошла ошибка валидации: {}.", e.getMessage());
            throw e;
        }
        if (filmService.notContainsFilm(film.getId())) {
            var e = new FilmNotFoundException("Не возможно обновить фильм. Фильма с таким id не существует в базе.");
            log.error("При обработке запроса PUT /films произошла ошибка: {}, {}.", e.getMessage(), film.getId());
            throw e;
        }
        var updatedFilm = filmService.updFilm(film);
        log.debug("Обработка запроса PUT /films. Фильм обновлен: {}.", updatedFilm);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        var allFilms = filmService.getAllFilms();
        var size = allFilms.size();
        log.debug("Обработка запроса GET /films. Текущее количество фильмов: {}", size);
        return allFilms;
    }

    @GetMapping("/{Id}")
    public Film getFilmById(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new IncorrectParameterException("Параметр id имеет отрицательное значение.");
        }
        if (filmService.notContainsFilm(id)) {
            var e = new FilmNotFoundException("Не удалось получить данные фильма. Фильма с таким id не существует в базе.");
            log.error("При обработке запроса GET /films/{filmId} произошла ошибка: {}, {}", e.getMessage(), id);
            throw e;
        }
        var filmGetById = filmService.getFilmById(id);
        log.debug("Обработка запроса GET /films/{id}. Получены данные фильма: {}.", filmGetById);
        return filmGetById;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new IncorrectParameterException("Параметр id имеет отрицательное значение.");
        }
        if (userId == null) {
            throw new IncorrectParameterException("Параметр userId равен null.");
        }
        if (userId <= 0) {
            throw new IncorrectParameterException("Параметр userId имеет отрицательное значение.");
        }
        filmService.addLike(id, userId);
        log.debug("Обработка запроса PUT /films/{id}/like/{userId}. Обновлены данные фильма с id: {}.", id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (id <= 0) {
            throw new IncorrectParameterException("Параметр id имеет отрицательное значение.");
        }
        if (userId == null) {
            throw new IncorrectParameterException("Параметр userId равен null.");
        }
        if (userId <= 0) {
            throw new IncorrectParameterException("Параметр userId имеет отрицательное значение.");
        }
        filmService.delLike(id, userId);
        log.debug("Обработка запроса DELETE /films/{id}/like/{userId}. Обновлены данные фильма с id: {}.", id);
    }

    @GetMapping(value = {"/popular?count={count}", "/popular"})
    public List<Film> getTopFilms(@PathVariable Optional<Integer> count) {
        if (count.isEmpty()) {
            throw new IncorrectParameterException("Параметр count равен null.");
        }
        if (count.get() < 0) {
            throw new IncorrectParameterException("Параметр count имеет отрицательное значение.");
        }
        var topFilmList = filmService.getTopCountFilmsOrTop10Films(count);
        log.debug("Обработка запроса GET /films/popular?count={count}.");
        return topFilmList;
    }
}
