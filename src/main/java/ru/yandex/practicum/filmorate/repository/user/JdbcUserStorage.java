package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utility.CommonHelper;

import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class JdbcUserStorage implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public User addUser(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "INSERT INTO USERS (USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY)"
                + "VALUES (:email, :login, :name, :birthday)";
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        jdbc.update(sqlQuery, params, keyHolder, new String[] {"USER_ID"});
        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(generatedId);
        log.info("Создан пользователь ={}", user.getId());
        CommonHelper.setUsersCount(getMaxUserCount());
        return user;
    }

    @Override
    public User update(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "UPDATE USERS SET USER_EMAIL = :email, USER_LOGIN = :login, " +
                "USER_NAME = :name, USER_BIRTHDAY = :birthday " +
                "WHERE USER_ID = :userId";
        params.addValue("userId", user.getId());
        params.addValue("email", user.getEmail());
        params.addValue("login", user.getLogin());
        params.addValue("name", user.getName());
        params.addValue("birthday", user.getBirthday());
        int rowsAffected = jdbc.update(sqlQuery, params);
        if (rowsAffected == 0) {
            throw new EntityNotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }
        log.info("Обновлен пользователь ={}", user.getId());
        return user;
    }

    @Override
    public List<Long> getAll() {
        String sqlQuery = "SELECT USER_ID FROM USERS";
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.queryForList(sqlQuery, params, Long.class);
    }

    @Override
    public User get(Long id) {
        String sqlQuery = "SELECT USER_ID, USER_EMAIL, USER_LOGIN, USER_NAME, USER_BIRTHDAY FROM USERS WHERE USER_ID = :userId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", id);
        return jdbc.queryForObject(sqlQuery, params, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("USER_ID"));
            user.setEmail(rs.getString("USER_EMAIL"));
            user.setLogin(rs.getString("USER_LOGIN"));
            user.setName(rs.getString("USER_NAME"));
            user.setBirthday(rs.getDate("USER_BIRTHDAY").toLocalDate());
            return user;
        });
    }

    public Long getMaxUserCount() {
        String sqlQuery = "SELECT MAX(USER_ID) FROM USERS";
         Long number = jdbc.queryForObject(sqlQuery, new HashMap<>(), Long.class);
         return number == null ? 0 : number;
        //метод для получения count всех Users, что бы обновлять число только при создании пользователя и снизить
        //количество обращений к БД.
    }
}
