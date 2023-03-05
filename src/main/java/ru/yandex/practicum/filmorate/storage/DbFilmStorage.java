package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
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

        for (int userId : film.getLikesSet()) {
            jdbcTemplate.update("INSERT INTO FILMS_USERS_LIKES (FILM_ID, USER_ID) VALUES (?, ?)", filmDbId, userId);
        }
        return getById(filmDbId);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, F.MPA_ID, M.CODE, DURATION FROM FILMS AS F INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public Film getById(int id) {
        String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, F.MPA_ID, M.CODE, DURATION FROM FILMS AS F INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID WHERE FILM_ID = ?";

        List<Film> result = jdbcTemplate.query(sql, new FilmRowMapper(), id);
        if (result.isEmpty()) {
            log.error("Ошибка! Не найден фильм с id: {}.", id);
            throw new FilmNotFoundException("Не найден фильм с id: " + id);
        }
        return result.get(0);
    }

    @Override
    public Film update(Film film) {
        String checkId = "SELECT FILM_ID FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet checkResult = jdbcTemplate.queryForRowSet(checkId, film.getId());
        if (checkResult.next()) {
            var foundId = checkResult.getInt("FILM_ID");
            log.info("Фильм с id {} найден, обновление.", foundId);
        } else {
            log.error("Ошибка! Не найден фильм с id: {}.", film.getId());
            throw new FilmNotFoundException("Не возможно обновить фильм. Не найден фильм c id: " + film.getId());
        }

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
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film(rs.getInt("FILM_ID"));
            film.setName(rs.getString("FILM_NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            Mpa mpa = new Mpa(rs.getInt("MPA_ID"), rs.getString("CODE"));
            film.setMpa(mpa);

            film.setDuration(rs.getInt("DURATION"));
            String sqlGenre = "SELECT FG.GENRE_ID, G.GENRE_NAME FROM FILM_GENRE AS FG INNER JOIN GENRE AS G ON FG.GENRE_ID = G.GENRE_ID WHERE FILM_ID = ?";
            List<Genre> genreList = jdbcTemplate.query(sqlGenre, (rsG, rowNumG) ->
                    new Genre(rsG.getInt("GENRE_ID"), rsG.getString("GENRE_NAME")), film.getId());
            film.setGenres(new HashSet<>(genreList));

            String sqlLikes = "SELECT USER_ID FROM FILMS_USERS_LIKES WHERE FILM_ID = ?";
            List<Integer> likesIdList = jdbcTemplate.query(sqlLikes, (rsL, rowNumL) ->
                    rsL.getInt("USER_ID"), film.getId());
            film.setLikesSet(new HashSet<>(likesIdList));
            return film;
        }
    }
}
