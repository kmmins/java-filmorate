package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final FilmService filmService;

    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Mpa> getAllMpa() {
        var allMpa = filmService.getAllMpa();
        log.debug("Обработка запроса GET /mpa. Получены все имеющиеся на данный момент рейтинги Американской киноассоциации.");
        return allMpa;
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable Integer id) {
        var mpa = filmService.getMpaById(id);
        log.debug("Обработка запроса GET /mpa/{id}. Получен рейтинг оценки содержания фильма по указанному id: {}.", id);
        return mpa;
    }
}
