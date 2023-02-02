package ru.yandex.practicum.filmorate.model;

public class SomeError {
    private final String error;

    public SomeError(String error) {
        this.error = error;
    }

    public String getError(){
        return error;
    }
}
