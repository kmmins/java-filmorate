package ru.yandex.practicum.filmorate.exception;

public class UserAlreadyExistException extends IllegalArgumentException {
    public UserAlreadyExistException (String message) {
        super(message);
    }
}
