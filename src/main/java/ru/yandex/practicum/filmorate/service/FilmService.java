package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Film film, User user) {
        film.getLikesSet().add(user.getId());
    }

    public void delLike(Film film, User user) {
        film.getLikesSet().remove(user.getId());
    }

    public Set<Integer> getLikes(Film film) {
        return film.getLikesSet();
    }

    public List<Film> getTop10Films() {
        List<Film> top10FilmList;
        List<Film> allFilms = filmStorage.getAllFilms();

        Comparator<Film> filmLikesComparator = Comparator.comparing(
                Film::getLikesSet, (s1, s2) -> compare(s2.size(), s1.size())
        );

        top10FilmList = allFilms.stream()
                .filter(film -> !film.getLikesSet().isEmpty())
                .limit(10)
                .sorted(filmLikesComparator)
                .collect(Collectors.toList());

        return top10FilmList;
    }
}