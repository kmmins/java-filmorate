package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int id, int userId) {
        var film = filmStorage.getFilmById(id);

        film.getLikesSet().add(userId);
    }

    public void delLike(int id, int userId) {
        var film = filmStorage.getFilmById(id);

        film.getLikesSet().remove(userId);
    }

    public List<Film> getTopCountFilmsOrTop10Films(Optional<Integer> count) {
        List<Film> topCountFilms;
        List<Film> allFilms = filmStorage.getAllFilms();
        Comparator<Film> filmLikesComparator = Comparator.comparing(
                Film::getLikesSet, (s1, s2) -> compare(s2.size(), s1.size())
        );

        topCountFilms = allFilms.stream()
                .sorted(filmLikesComparator)
                .limit(count.orElse(10))
                .collect(Collectors.toList());
        return topCountFilms;
    }
}