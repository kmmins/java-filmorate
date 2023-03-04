package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DbGenreStorage implements AbstractStorage<Genre> {

    private final JdbcTemplate jdbcTemplate;

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE";
        return jdbcTemplate.query(sql, new GenreRowMapper());
    }

    @Override
    public Genre getById(int id) {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE";
        List<Genre> result = jdbcTemplate.query(sql, new GenreRowMapper());
        return result.get(0);
    }

    @Override
    public Genre add(Genre genre) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Genre update(Genre genre) {
        throw new UnsupportedOperationException();
    }

    private class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getInt("GENRE_ID"), rs.getString("GENRE_NAME"));
        }
    }
}
