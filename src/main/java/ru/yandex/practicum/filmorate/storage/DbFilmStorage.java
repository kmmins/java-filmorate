package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("dbFilmStorage")
public class DbFilmStorage implements AbstractStorage<Film> {

    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert create = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        int filmDbId = create.executeAndReturnKey(Map.of(
                "FILM_NAME", film.getName(),
                "DESCRIPTION", film.getDescription(),
                "RELEASE_DATE", film.getReleaseDate(),
                "MPA_ID", film.getMpa().getId(),
                "DURATION", film.getDuration())

        ).intValue();

        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)", filmDbId, genre.getId());
        }

        return getById(filmDbId);
    }

    @Override
    public List<Film> getAll() {
        String sqlGa = "SELECT F.FILM_ID," +
                "FILM_NAME," +
                "DESCRIPTION," +
                "RELEASE_DATE," +
                "FG.GENRE_ID," +
                "G.GENRE_NAME," +
                "F.MPA_ID," +
                "M.CODE," +
                "DURATION," +
                "FUL.USER_ID " +
                "FROM FILMS AS F " +
                "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE_ID = G.GENRE_ID " +
                "LEFT OUTER JOIN FILMS_USERS_LIKES AS FUL ON F.FILM_ID = FUL.FILM_ID";

        FilmRowMapper mapper = new FilmRowMapper();
        jdbcTemplate.query(sqlGa, mapper);

        return new ArrayList<>(mapper.getHm().values());
    }

    @Override
    public Film getById(int id) {
        String sqlGb = "SELECT F.FILM_ID," +
                "FILM_NAME," +
                "DESCRIPTION," +
                "RELEASE_DATE," +
                "FG.GENRE_ID," +
                "G.GENRE_NAME," +
                "F.MPA_ID," +
                "M.CODE," +
                "DURATION," +
                "FUL.USER_ID " +
                "FROM FILMS AS F " +
                "INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRE AS FG ON F.FILM_ID = FG.FILM_ID " +
                "LEFT OUTER JOIN GENRE AS G ON FG.GENRE_ID = G.GENRE_ID " +
                "LEFT OUTER JOIN FILMS_USERS_LIKES AS FUL ON F.FILM_ID = FUL.FILM_ID WHERE F.FILM_ID = ?";

        FilmRowMapper mapper = new FilmRowMapper();
        jdbcTemplate.query(sqlGb, mapper, id);

        return mapper.getHm().get(id);
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, MPA_ID = ?, DURATION = ? WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getDuration(),
                film.getId());

        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genre.getId());
        }

        jdbcTemplate.update("DELETE FROM FILMS_USERS_LIKES WHERE FILM_ID = ?", film.getId());
        for (int userId : film.getLikesSet()) {
            jdbcTemplate.update("INSERT INTO FILMS_USERS_LIKES (FILM_ID, USER_ID) VALUES (?, ?)", film.getId(), userId);
        }
        return getById(film.getId());
    }

    private class FilmRowMapper implements RowMapper<Film> {

        private final Map<Integer, Film> hm;

        public FilmRowMapper() {
            hm = new HashMap<>();
        }

        public Map<Integer, Film> getHm() {
            return hm;
        }

        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            int filmId = rs.getInt("FILM_ID");

            if (!hm.containsKey(filmId)) {
                hm.put(filmId, new Film(filmId));
            }
            var film = hm.get(filmId);
            film.setName(rs.getString("FILM_NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            Mpa mpa = new Mpa(rs.getInt("MPA_ID"), rs.getString("CODE"));
            film.setMpa(mpa);
            film.setDuration(rs.getInt("DURATION"));

            if (rs.getObject("GENRE_ID") != null) {
                Genre genre = new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
                film.getGenres().add(genre);
            }

            if (rs.getObject("USER_ID") != null) {
                Integer like = rs.getInt("USER_ID");
                film.getLikesSet().add(like);
            }
            return film;
        }
    }
}
