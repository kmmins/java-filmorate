package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    boolean containsFilm(Film film);

    Film updFilm(Film film);

    List<Film> getAllFilms();
}
