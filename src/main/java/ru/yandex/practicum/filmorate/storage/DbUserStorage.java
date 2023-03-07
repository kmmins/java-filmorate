package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("dbUserStorage")
public class DbUserStorage implements AbstractStorage<User> {

    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User add(User user) {
        SimpleJdbcInsert create = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        int userDbId = create.executeAndReturnKey(Map.of(
                        "EMAIL", user.getEmail(),
                        "LOGIN", user.getLogin(),
                        "USER_NAME", user.getName(),
                        "BIRTHDAY", user.getBirthday()
                )
        ).intValue();

        var thisUserFriendsMap = user.getFriendsMap();
        for (Map.Entry<Integer, Boolean> elm : thisUserFriendsMap.entrySet()) {
            jdbcTemplate.update("INSERT INTO FRIENDS (CONFIRMED, USER_ID_THIS, USER_ID_OTHER) VALUES (?, ?, ?)",
                    elm.getValue(), user.getId(), elm.getKey());
        }
        return getById(userDbId);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    @Override
    public User getById(int id) {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, USER_NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?";
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper(), id);
        if (result.isEmpty()) {
            log.error("Ошибка! Не найден пользователь с id: {}.", id);
            throw new UserNotFoundException(String.format("Не найден пользователь с id %d", id));
        }
        return result.get(0);
    }

    @Override
    public User update(User user) {
        String check = "SELECT USER_ID FROM USERS WHERE USER_ID = ?";
        SqlRowSet checkResult = jdbcTemplate.queryForRowSet(check, user.getId());
        if (checkResult.next()) {
            var foundId = checkResult.getInt("USER_ID");
            log.info("Пользователь с id {} найден, обновление.", foundId);
        } else {
            log.error("Ошибка! Не найден пользователь с id: {}.", user.getId());
            throw new UserNotFoundException(String.format("Не возможно обновить данные. Не найден пользователь с id %d.", user.getId()));
        }

        jdbcTemplate.update("UPDATE USERS SET EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID_THIS = ?", user.getId());

        var thisUserFriendsMap = user.getFriendsMap();
        for (Map.Entry<Integer, Boolean> elm : thisUserFriendsMap.entrySet()) {
            jdbcTemplate.update("INSERT INTO FRIENDS (CONFIRMED, USER_ID_THIS, USER_ID_OTHER) VALUES (?, ?, ?)",
                    elm.getValue(), user.getId(), elm.getKey());
        }
        return getById(user.getId());
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getInt("USER_ID"));
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setName(rs.getString("USER_NAME"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());

            String sqlFm = "SELECT USER_ID_OTHER, CONFIRMED FROM FRIENDS WHERE USER_ID_THIS = ?";
            HashMap<Integer, Boolean> friendsMap = new HashMap<>();
            jdbcTemplate.query(sqlFm, (rsFm, rowNumFm) ->
                    friendsMap.put(rsFm.getInt("USER_ID_OTHER"), rsFm.getBoolean("CONFIRMED")), user.getId());
            user.setFriendsMap(friendsMap);

            return user;
        }
    }
}
