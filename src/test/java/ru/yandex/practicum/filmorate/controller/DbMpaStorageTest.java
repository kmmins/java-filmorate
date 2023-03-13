package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DbMpaStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbMpaStorageTest {

    private final DbMpaStorage mpaStorage;

    @Test
    public void checkAddMpa(){
        Mpa testMpa = new Mpa(6, "Test");
        assertThrows(UnsupportedOperationException.class, ()-> mpaStorage.add(testMpa));
    }
    @Test
    public void checkUpdateMpa(){
        Mpa testMpa = new Mpa(6, "Test");
        assertThrows(UnsupportedOperationException.class, ()-> mpaStorage.update(testMpa));
    }

    @Test
    public void getAllGenre() {
        var listAllMpa = mpaStorage.getAll();
        assertEquals(5, listAllMpa.size(), "Некорректное количество рейтингов MPA.");
    }

    @Test
    public void getByIdGenre() {
        var mpaIdNumberFive = mpaStorage.getById(5);
        assertEquals("NC-17", mpaIdNumberFive.getName(), "Некорректный рейтинг MPA.");
    }
}
