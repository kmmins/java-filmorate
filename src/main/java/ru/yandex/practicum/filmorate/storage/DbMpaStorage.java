package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class DbMpaStorage implements AbstractStorage<Mpa> {

    private final JdbcTemplate jdbcTemplate;

    public DbMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT MPA_ID, CODE FROM MPA";
        return jdbcTemplate.query(sql, new MpaRowMapper());
    }

    @Override
    public Mpa getById(int id) {
        String sql = "SELECT MPA_ID, CODE FROM MPA WHERE MPA_ID = ?";
        List<Mpa> result = jdbcTemplate.query(sql, new MpaRowMapper());
        return result.get(0);
    }

    @Override
    public Mpa add(Mpa mpa) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mpa update(Mpa mpa) {
        throw new UnsupportedOperationException();
    }

    private class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Mpa(rs.getInt("MPA_ID"), rs.getString("CODE"));
        }
    }
}
