package ru.yandex.practicum.filmorate.model;

public class CustomError {
    private final String error;

    public CustomError(String error) {
        this.error = error;
    }

    public String getError(){
        return error;
    }
}
