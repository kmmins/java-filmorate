package ru.yandex.practicum.filmorate.exception;

public class FilmAlreadyExistException extends IllegalArgumentException {
    public FilmAlreadyExistException(String message) {
        super(message);
    }
}
