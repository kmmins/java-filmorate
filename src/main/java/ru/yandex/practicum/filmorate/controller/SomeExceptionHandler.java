package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.SomeIncorrectException;
import ru.yandex.practicum.filmorate.model.SomeError;

@RestControllerAdvice
public class SomeExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND) // BAD_REQUEST и др. http статусы
    public SomeError handleSomething (final SomeIncorrectException e) {
        return new SomeError(
                e.getMessage());
    }
}
