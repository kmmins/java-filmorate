package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements AbstractStorage<Film> {

    private int countFilms = 0;
    private final HashMap<Integer, Film> filmsDatabase = new HashMap<>();

    private boolean notContainsFilm(int id) {
        return !filmsDatabase.containsKey(id);
    }

    @Override
    public Film add(Film film) {
        countFilms++;
        var createdFilm = new Film(
                countFilms,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getGenre(),
                film.getMpa(),
                film.getDuration(),
                film.getLikesSet()
        );
        filmsDatabase.put(countFilms, createdFilm);
        return createdFilm;
    }

    @Override
    public Film update(Film film) {
        if (notContainsFilm(film.getId())) {
            throw new FilmNotFoundException("Не возможно обновить фильм. Не найден фильм c id: " + film.getId());
        }
        filmsDatabase.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(filmsDatabase.values());
    }

    @Override
    public Film getById(int id) {
        var result = filmsDatabase.get(id);
        if (result == null) {
            throw new FilmNotFoundException("Не найден фильм с id: " + id);
        }
        return result;
    }
}
