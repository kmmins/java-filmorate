package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sql = "SELECT U.USER_ID," +
                "EMAIL," +
                "LOGIN," +
                "USER_NAME," +
                "BIRTHDAY," +
                "F.CONFIRMED," +
                "F.USER_ID_OTHER " +
                "FROM USERS AS U LEFT OUTER JOIN FRIENDS AS F ON U.USER_ID = F.USER_ID_THIS";

        UserRowMapper mapper = new UserRowMapper();
        jdbcTemplate.query(sql, mapper);

        return new ArrayList<>(mapper.getUm().values());
    }

    @Override
    public User getById(int id) {
        String sql = "SELECT U.USER_ID," +
                "EMAIL," +
                "LOGIN," +
                "USER_NAME," +
                "BIRTHDAY," +
                "F.CONFIRMED," +
                "F.USER_ID_OTHER " +
                "FROM USERS AS U LEFT OUTER JOIN FRIENDS AS F ON U.USER_ID = F.USER_ID_THIS WHERE U.USER_ID = ?";

        UserRowMapper mapper = new UserRowMapper();
        jdbcTemplate.query(sql, mapper, id);

        return mapper.getUm().get(id);
    }

    @Override
    public User update(User user) {
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
        private final Map<Integer, User> um;
        public UserRowMapper() {
            um = new HashMap<>();
        }
        public Map<Integer, User> getUm() {
            return um;
        }

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            int userID = rs.getInt("USER_ID");

            if (!um.containsKey(userID)) {
                um.put(userID, new User(userID));
            }

            var user = um.get(userID);
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setName(rs.getString("USER_NAME"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());

            if (rs.getObject("USER_ID_OTHER") != null) {
                user.getFriendsMap().put(rs.getInt("USER_ID_OTHER"), rs.getBoolean("CONFIRMED"));
            }
            return user;
        }
    }
}
