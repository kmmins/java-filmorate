package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

import javax.validation.ValidationException;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    // 400 Bad Request («неправильный, некорректный запрос»)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIncorrectParameter(final IncorrectParameterException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return Map.of("error", "Ошибка с параметром id.",
                "errorMessage", e.getMessage()
        );
    }

    //400 Bad Request («неправильный, некорректный запрос»)
    @ExceptionHandler({ValidationException.class, CustomValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleNotValid(final RuntimeException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return Map.of(
                "error", "Некорректный запрос.",
                "errorMessage", e.getMessage()
        );
    }

    //404 Not Found («не найдено»)
    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(final RuntimeException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return Map.of(
                "error", "Ничего не найдено.",
                "errorMessage", e.getMessage()
        );
    }

    //409 Conflict («конфликт»)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAlreadyExist(final UserAlreadyExistException e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return Map.of(
                "error", "Конфликт.",
                "errorMessage", e.getMessage()
        );
    }

    //500 Internal Server Error («внутренняя ошибка сервера»)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleServerError(final Throwable e) {
        log.error("При обработке запроса возникла ошибка: {}.", e.getMessage());
        return Map.of(
                "error", "Произошла непредвиденная ошибка.",
                "errorMessage", e.getMessage()
        );
    }
}
