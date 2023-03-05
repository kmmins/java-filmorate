package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.DbGenreStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbGenreStorageTest {

    private final DbGenreStorage genreStorage;

    @Test
    public void checkAddGenre() {
        Genre testGenre = new Genre(7, "Test");
        assertThrows(UnsupportedOperationException.class, ()-> genreStorage.add(testGenre));
    }

    @Test
    public void checkUpdateGenre() {
        Genre testGenre = new Genre(7, "Test");
        assertThrows(UnsupportedOperationException.class, ()-> genreStorage.update(testGenre));
    }

    @Test
    public void checkGetAllGenre() {
        var listAllGenre = genreStorage.getAll();
        assertEquals(6, listAllGenre.size(), "Некорректное количество жанров.");
    }

    @Test
    public void checkGetByIdGenre() {
        var genreIdNumberTwo = genreStorage.getById(2);
        assertEquals("Драма", genreIdNumberTwo.getName(), "Некорректное имя жанра.");

    }
}
