package ru.yandex.practicum.filmorate.model;

public enum Rating {
    G,
    PG,
    PG13,
    R,
    NC17;

    @Override
    public String toString() {
        if (this.equals(Rating.G)) {
            return "G";
        } else if (this.equals(Rating.PG)) {
            return "PG";
        } else if (this.equals(Rating.PG13)) {
            return "PG-13";
        } else if (this.equals(Rating.R)) {
            return "R";
        } else if (this.equals(Rating.NC17)) {
            return "NC-17";
        }
        throw new RuntimeException("Неправильный указан тип рейтинга.");
    }
}
