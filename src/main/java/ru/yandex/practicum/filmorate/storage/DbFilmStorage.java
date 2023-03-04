package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

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

    //create////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Film add(Film film) {
        SimpleJdbcInsert create = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        int filmDbId = create.executeAndReturnKey(Map.of(
                "FILM_NAME", film.getName(),
                "DESCRIPTION", film.getDescription(),
                "RELEASE_DATE", film.getReleaseDate(),
                "MPA_ID", film.getMpa(),
                "DURATION", film.getDuration())
        ).intValue();

        for (Integer genreId : film.getGenre()) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)", filmDbId, genreId);
        }

        for (int userId : film.getLikesSet()) {
            jdbcTemplate.update("INSERT INTO FILMS_USERS_LIKES (FILM_ID, USER_ID) VALUES (?, ?)", filmDbId, userId);
        }

        return new Film(
                filmDbId,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getGenre(),
                film.getMpa(),
                film.getDuration(),
                film.getLikesSet()
        );
    }

    //read//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public List<Film> getAll() {
        String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, MPA_ID, DURATION FROM FILMS";
        return jdbcTemplate.query(sql, new FilmRowMapper());
    }

    @Override
    public Film getById(int id) {
        String sql = "SELECT FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, MPA_ID, DURATION FROM FILMS WHERE FILM_ID = ?";

        List<Film> result = jdbcTemplate.query(sql, new FilmRowMapper(), id);
        if (result.isEmpty()) {
            throw new FilmNotFoundException("Не найден фильм с id: " + id);
        }
        return result.get(0);
    }

    //update////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public Film update(Film film) {
        String check = "SELECT FILM_ID FROM FILMS WHERE FILM_ID = ?";
        SqlRowSet checkResult = jdbcTemplate.queryForRowSet(check, film.getId());
        if (checkResult.next()) {
            var foundId = checkResult.getInt("FILM_ID");
            log.info("Фильм с id {} найден, обновление.", foundId);
        } else {
            throw new FilmNotFoundException("Не возможно обновить фильм. Не найден фильм c id: " + film.getId());
        }

        jdbcTemplate.update("UPDATE FILMS SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, MPA_ID = ?, DURATION = ? WHERE FILM_ID = ?",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getMpa(),
                film.getDuration(),
                film.getId());

        jdbcTemplate.update("DELETE FROM FILM_GENRE WHERE FILM_ID = ?", film.getId());

        for (Integer genreId : film.getGenre()) {
            jdbcTemplate.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)", film.getId(), genreId);
        }

        jdbcTemplate.update("DELETE FROM FILMS_USERS_LIKES WHERE FILM_ID = ?", film.getId());

        for (int userId : film.getLikesSet()) {
            jdbcTemplate.update("INSERT INTO FILMS_USERS_LIKES (FILM_ID, USER_ID) VALUES (?, ?)", film.getId(), userId);
        }
        return film;
    }

    //RowMapper/////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            Film film = new Film(rs.getInt("FILM_ID"));
            film.setName(rs.getString("FILM_NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            film.setMpa(rs.getInt("MPA_ID"));
            film.setDuration(rs.getInt("DURATION"));

            String sqlGenre = "SELECT GENRE_ID FROM FILM_GENRE WHERE FILM_ID = ?";
            List<Integer> genreIdList = jdbcTemplate.query(sqlGenre, (rsG, rowNumG) ->
                    rsG.getInt("GENRE_ID"), film.getId());
            film.setGenre(new HashSet<>(genreIdList));

            String sqlLikes = "SELECT USER_ID FROM FILMS_USERS_LIKES WHERE FILM_ID = ?";
            List<Integer> likesIdList = jdbcTemplate.query(sqlLikes, (rsL, rowNumL) ->
                    rsL.getInt("USER_ID"), film.getId());
            film.setLikesSet(new HashSet<>(likesIdList));
            return film;
        }
    }
}
