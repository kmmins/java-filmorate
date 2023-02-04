package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film addFilm(Film film);

    /**
     * Вспомогательный метод
     * @param id фильма для проверки на содержание его в базе (памяти)
     * @return булево значение для фильма на содержание его в базе (памяти)
     */
    boolean notContainsFilm(int id);

    Film updFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int id);
}
