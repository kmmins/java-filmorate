package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    boolean containsFilm(int id);

    Film updFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int id);
}
