package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CustomValidationException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.CustomValidator;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
public class FilmController {

    private final CustomValidator validator;
    private final FilmService filmService;

    @Autowired
    public FilmController(CustomValidator validator, FilmService filmService) {
        this.validator = validator;
        this.filmService = filmService;
    }

    @PostMapping("/films")
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

    @PutMapping("/films")
    public Film updFilm(@Valid @RequestBody Film film) {
        try {
            validator.validateFilms(film);
        } catch (CustomValidationException e) {
            log.error("При обработке запроса PUT /films произошла ошибка валидации: {}.", e.getMessage());
            throw e;
        }
        var updatedFilm = filmService.updFilm(film);
        log.debug("Обработка запроса PUT /films. Фильм обновлен: {}.", updatedFilm);
        return updatedFilm;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        var allFilms = filmService.getAllFilms();
        var size = allFilms.size();
        log.debug("Обработка запроса GET /films. Текущее количество фильмов: {}.", size);
        return allFilms;
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        var filmGetById = filmService.getFilmById(id);
        log.debug("Обработка запроса GET /films/{id}. Получены данные фильма: {}.", filmGetById);
        return filmGetById;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (userId == null) {
            throw new IncorrectParameterException("Параметр userId равен null.");
        }
        filmService.addLike(id, userId);
        log.debug("Обработка запроса PUT /films/{id}/like/{userId}. Обновлены данные фильма с id: {}.", id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void delLike(@PathVariable Integer id, @PathVariable Integer userId) {
        if (id == null) {
            throw new IncorrectParameterException("Параметр id равен null.");
        }
        if (userId == null) {
            throw new IncorrectParameterException("Параметр userId равен null.");
        }
        filmService.delLike(id, userId);
        log.debug("Обработка запроса DELETE /films/{id}/like/{userId}. Обновлены данные фильма с id: {}.", id);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(required = false) Integer count) {
        var topFilmList = filmService.getTopCountFilmsOrTop10Films(count);
        log.debug("Обработка запроса GET /films/popular?count={count}. Получены данные популярных фильмов.");
        return topFilmList;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenre() {
        var allGenre = filmService.getAllGenre();
        log.debug("Обработка запроса GET /genres. Получены все имеющиеся жанры фильмов на данный момент.");
        return allGenre;
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        var genre = filmService.getGenreById(id);
        log.debug("Обработка запроса GET /genres/{id}. Получен жанр фильма по указанному id: {}.", id);
        return genre;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        var allMpa = filmService.getAllMpa();
        log.debug("Обработка запроса GET /mpa. Получены все имеющиеся на данный момент рейтинги Американской киноассоциации.");
        return allMpa;
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable Integer id) {
        var mpa = filmService.getMpaById(id);
        log.debug("Обработка запроса GET /mpa/{id}. Получен рейтинг оценки содержания фильма по указанному id: {}.", id);
        return mpa;
    }
}
