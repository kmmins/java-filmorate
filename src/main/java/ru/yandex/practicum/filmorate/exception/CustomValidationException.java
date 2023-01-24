package ru.yandex.practicum.filmorate.exception;

public class CustomValidationException extends IllegalArgumentException {
    public CustomValidationException (String message) {
        super(message);
    }
}
