package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
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

    //create////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public User add(User user) {
        SimpleJdbcInsert create = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");
        int userDbId = create.executeAndReturnKey(Map.of(
                        "EMAIL", user.getEmail(),
                        "LOGIN", user.getLogin(),
                        "NAME", user.getName(),
                        "BIRTHDAY", user.getBirthday()
                )
        ).intValue();

        var thisUserFriendsMap = user.getFriendsMap();
        for (Map.Entry<Integer, Boolean> elm : thisUserFriendsMap.entrySet()) {
            jdbcTemplate.update("INSERT INTO FRIENDS (CONFIRMED, USER_THIS, USER_OTHER) VALUES (?, ?, ?)",
                    elm.getValue(), user.getId(), elm.getKey());
        }

        User dbCreatedUser = new User(
                userDbId,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriendsMap()
        );
        return dbCreatedUser;
    }

    //read//////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public List<User> getAll() {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS";
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper());
        return result;
    }

    @Override
    public User getById(int id) {
        String sql = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY FROM USERS WHERE USER_ID = ?";
        List<User> result = jdbcTemplate.query(sql, new UserRowMapper());
        if (result.isEmpty()) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        return result.get(0);
    }

    //update////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public User update(User user) {
        String check = "SELECT USER_ID FROM USERS WHERE USER_ID = ?";
        SqlRowSet checkResult = jdbcTemplate.queryForRowSet(check, user.getId());
        if (checkResult.next()) {
            var findId = checkResult.getInt("USER_ID");
            log.info("Пользователь с id "+ findId +" найден, обновление.");
        } else {
            throw new UserNotFoundException("Не возможно обновить данные пользователя. Не найден пользователь с id: " + user.getId());
        }

        jdbcTemplate.update("UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_THIS = ?", user.getId());

        var thisUserFriendsMap = user.getFriendsMap();
        for (Map.Entry<Integer, Boolean> elm : thisUserFriendsMap.entrySet()) {
            jdbcTemplate.update("INSERT INTO FRIENDS (CONFIRMED, USER_THIS, USER_OTHER) VALUES (?, ?, ?)",
                    elm.getValue(), user.getId(), elm.getKey());
        }
        return user;
    }

    //RowMapper/////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getInt("USER_ID"));
            user.setEmail(rs.getString("EMAIL"));
            user.setLogin(rs.getString("LOGIN"));
            user.setName(rs.getString("NAME"));
            user.setBirthday(rs.getDate("BIRTHDAY").toLocalDate());

            /*Map<Integer, Boolean> userFriendsMap = jdbcTemplate.queryForObject(sqlF, (rsF, rowNumF) ->
                    Map.of(rsF.getInt("USER_OTHER"), rsF.getBoolean("CONFIRMED")), user.getId());*/
            /*List<Map<Integer, Boolean>> userFriendsMapList = jdbcTemplate.query(sqlF, (rsF, rowNumF) ->
                    Map.of(rsF.getInt("USER_OTHER"), rsF.getBoolean("CONFIRMED")), user.getId());*/
            /*List<Map<Integer, Boolean>> userFriendsMapList = new ArrayList<>();
            SqlRowSet userFriendsMapRow = jdbcTemplate.queryForRowSet(sqlF, user.getId());
                while (userFriendsMapRow.next()){
                    Map.of(userFriendsMapRow.getInt("USER_OTHER"), userFriendsMapRow.getBoolean("CONFIRMED")));
                }*/

            HashMap<Integer, HashMap<Integer, Boolean>> allUsersFriendsMap = new HashMap<>();
            String sqlFm = "SELECT USER_OTHER, CONFIRMED FROM FRIENDS WHERE USER_THIS = ?";
            SqlRowSet fmRow = jdbcTemplate.queryForRowSet(sqlFm, user.getId());
            while (fmRow.next()) {
                HashMap<Integer, Boolean> fm = new HashMap<>();
                fm.put(fmRow.getInt("USER_OTHER"), fmRow.getBoolean("CONFIRMED"));
                allUsersFriendsMap.put(user.getId(), fm);
            }

            user.setFriendsMap(allUsersFriendsMap.get(user.getId()));

            return user;
        }
    }
}
