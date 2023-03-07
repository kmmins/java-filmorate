package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/genres")
public class GenreController {

    private final FilmService filmService;

    @Autowired
    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Genre> getAllGenre() {
        var allGenre = filmService.getAllGenre();
        log.debug("Обработка запроса GET /genres. Получены все имеющиеся жанры фильмов на данный момент.");
        return allGenre;
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Integer id) {
        var genre = filmService.getGenreById(id);
        log.debug("Обработка запроса GET /genres/{id}. Получен жанр фильма по указанному id: {}.", id);
        return genre;
    }
}
