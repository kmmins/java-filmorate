package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    /**
     * Добавление фильма в базу (память).
     * @param film объект.
     * @return созданный фильм с уникальным id.
     */
    Film addFilm(Film film);

    /**
     * Вспомогательный метод.
     * @param id фильма для проверки на содержание его в базе (памяти).
     * @return булево значение для фильма на содержание его в базе (памяти).
     */
    boolean notContainsFilm(int id);

    /**
     * Обновление данных фильма в базе (памяти).
     * @param film объект.
     * @return фильм с обновленными данными.
     */
    Film updFilm(Film film);

    /**
     * Получение всех фильмов хранящихся в базе (памяти).
     * @return список фильмов.
     */
    List<Film> getAllFilms();

    /**
     * Получение фильма по уникальному идентификатору.
     * @param id уникальный идентификатор фильма.
     * @return объект типа фильм.
     */
    Film getFilmById(int id);
}
