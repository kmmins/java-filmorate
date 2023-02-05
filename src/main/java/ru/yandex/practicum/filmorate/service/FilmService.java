package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    //////////////////////////////////////////////методы filmStorage////////////////////////////////////////////////////

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updFilm(Film film) {
        return filmStorage.updFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    //////////////////////////////////////////////методы filmStorage////////////////////////////////////////////////////

    public void addLike(int id, int userId) {
        var film = filmStorage.getFilmById(id);

        if (userService.notContainsUser(userId)) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        film.getLikesSet().add(userId);
        filmStorage.updFilm(film);
    }

    public void delLike(int id, int userId) {
        var film = filmStorage.getFilmById(id);

        if (userService.notContainsUser(userId)) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        film.getLikesSet().remove(userId);
        filmStorage.updFilm(film);
    }

    public List<Film> getTopCountFilmsOrTop10Films(Integer count) {
        if (count != null && count <= 0) {
            throw new IncorrectParameterException("Параметр count имеет отрицательное значение.");
        }
        List<Film> topCountFilms;
        List<Film> allFilms = filmStorage.getAllFilms();
        Comparator<Film> filmLikesComparator = Comparator.comparing(
                Film::getLikesSet, (s1, s2) -> compare(s2.size(), s1.size())
        );
        int sizePopularList;
        sizePopularList = Objects.requireNonNullElse(count, 10);

        topCountFilms = allFilms.stream()
                .sorted(filmLikesComparator)
                .limit(sizePopularList)
                .collect(Collectors.toList());
        return topCountFilms;
    }
}