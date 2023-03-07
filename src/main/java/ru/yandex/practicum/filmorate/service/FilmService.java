package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Service
public class FilmService {

    private final AbstractStorage<Film> filmStorage;
    private final AbstractStorage<User> userStorage;
    private final AbstractStorage<Genre> genreStorage;
    private final AbstractStorage<Mpa> mpaStorage;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") AbstractStorage<Film> filmStorage,
                       @Qualifier("dbUserStorage") AbstractStorage<User> userStorage,
                       AbstractStorage<Genre> genreStorage,
                       AbstractStorage<Mpa> mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public Film addFilm(Film film) {
        return filmStorage.add(film);
    }

    public Film updFilm(Film film) {
        getFilmById(film.getId());

        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(int id) {
        var filmById = filmStorage.getById(id);
        if (filmById == null) {
            throw new FilmNotFoundException(String.format("Не найден фильм с id %d.", id));
        }
        return filmById;
    }

    public void addLike(int id, int userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id %d.", id));
        }
        var film = filmStorage.getById(id);

        film.getLikesSet().add(userId);
        filmStorage.update(film);
    }

    public void delLike(int id, int userId) {
        if (userStorage.getById(userId) == null) {
            throw new UserNotFoundException(String.format("Не найден пользователь с id %d.", id));
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

    public List<Genre> getAllGenre() {
        return genreStorage.getAll();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getById(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Mpa getMpaById(int id) {
        return mpaStorage.getById(id);
    }
}