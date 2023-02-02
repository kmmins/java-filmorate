package ru.yandex.practicum.filmorate.exception;

public class SomeIncorrectException extends RuntimeException {

    private final String some;

    public SomeIncorrectException (String some) {
        this.some = some;
    }

    public String getSome() {
        return some;
    }
}
