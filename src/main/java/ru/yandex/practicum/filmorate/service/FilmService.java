package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Service
public class FilmService {

    private final AbstractStorage<Film> filmStorage;
    private final AbstractStorage<User> userStorage;

    @Autowired
    public FilmService(AbstractStorage<Film> filmStorage, AbstractStorage<User> userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updFilm(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        return filmStorage.getById(id);
    }

    public void addLike(int id, int userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        var film = filmStorage.getById(id);

        film.getLikesSet().add(userId);
        filmStorage.update(film);
    }

    public void delLike(int id, int userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        var film = filmStorage.getById(id);
        film.getLikesSet().remove(userId);
        filmStorage.update(film);
    }

    public List<Film> getTopCountFilmsOrTop10Films(Integer count) {
        if (count != null && count <= 0) {
            throw new IncorrectParameterException("Параметр count имеет отрицательное значение.");
        }
        List<Film> topCountFilms;
        List<Film> allFilms = filmStorage.getAll();
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