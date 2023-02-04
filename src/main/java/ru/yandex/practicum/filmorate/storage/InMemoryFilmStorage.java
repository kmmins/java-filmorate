package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int countFilms = 0;
    private final HashMap<Integer, Film> filmsDatabase = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        countFilms++;
        var createdFilm = new Film(countFilms, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getLikesSet());
        filmsDatabase.put(countFilms, createdFilm);
        return createdFilm;
    }

    @Override
    public boolean notContainsFilm(int id) {
        return !filmsDatabase.containsKey(id);
    }

    @Override
    public Film updFilm(Film film) {
        filmsDatabase.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsDatabase.values());
    }

    @Override
    public Film getFilmById(int id) {
        return filmsDatabase.get(id);
    }
}
