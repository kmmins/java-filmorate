package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {

    int countFilms = 0;
    Validator validator = new Validator();
    private final HashMap<Integer, Film> filmsDatabase = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping("/film")
    public Film addFilm(@RequestBody Film film) {
        if (filmsDatabase.containsKey(film.getId())) {
            var e = new FilmAlreadyExistException("Не возможно добавить фильм. Фильм с таким id уже существует.");
            log.error("При обработке запроса GET /film произошла ошибка: " + e.getMessage());
            throw e;
        } else {
            try {
                validator.validateFilms(film);
                countFilms++;
                var addedFilm = new Film(countFilms);
                addedFilm.setName(film.getName());
                addedFilm.setDescription(film.getDescription());
                addedFilm.setReleaseDate(film.getReleaseDate());
                addedFilm.setDuration(film.getDuration());

                filmsDatabase.put(countFilms, addedFilm);
                log.debug("Добавлен фильм: {}.", addedFilm);
                return addedFilm;
            } catch (ValidationException e) {
                log.error("При обработке запроса GET /film произошла ошибка валидации: " + e.getMessage());
                throw e;
            }
        }
    }

    @PutMapping("/film")
    public Film updFilm(@RequestBody Film film) {
        if (filmsDatabase.containsKey(film.getId())) {
            try {
                validator.validateFilms(film);
                filmsDatabase.put(film.getId(), film);
                log.debug("Фильм обновлен: {}.", film);
                return film;
            } catch (ValidationException e) {
                log.error("При обработке запроса PUT /film произошла ошибка валидации: " + e.getMessage());
                throw e;
            }
        } else {
            var e = new FilmNotFoundException("Не возможно обновить фильм. Такого фильма не существует.");
            log.error("При обработке запроса PUT /film произошла ошибка: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        var getAllFilms = new ArrayList<>(filmsDatabase.values());
        var size = getAllFilms.size();
        log.debug("Обработка запроса GET /films: Текущее количество фильмов: {}", size);
        return getAllFilms;
    }
}
