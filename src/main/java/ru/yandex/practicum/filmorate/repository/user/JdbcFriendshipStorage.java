package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.FriendshipStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JdbcFriendshipStorage implements FriendShipRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addFriend(User masterUser, User userFriend) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "INSERT INTO FRIENDSHIP(STATUS_USER_ID_1, STATUS_USER_ID_2, STATUS_REQUEST) "
                + "VALUES (:masterUser, :userNewFriend, :status)";
        params.addValue("masterUser", masterUser.getId());
        params.addValue("userNewFriend", userFriend.getId());
        params.addValue("status", FriendshipStatus.CONFIRMED.name());
        jdbc.update(sqlQuery, params);
    }

    @Override
    public void removeFriend(User masterUser, User userFriend) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE STATUS_USER_ID_1 = :masterUser " +
                "and STATUS_USER_ID_2 = :userNewFriend";
        params.addValue("masterUser", masterUser.getId());
        params.addValue("userNewFriend", userFriend.getId());
        jdbc.update(sqlQuery, params);
    }

    @Override
    public Map<Long, FriendshipStatus> setFriendship(Long user) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "SELECT * FROM FRIENDSHIP WHERE STATUS_USER_ID_1 = :statusUserId";
        params.addValue("statusUserId", user);
        Map<Long, FriendshipStatus> friendshipStatusMap = new LinkedHashMap<>();
        jdbc.query(sqlQuery, params, (rs) -> {
            Long userFriendId = rs.getLong("STATUS_USER_ID_2");
            String statusRequest = rs.getString("STATUS_REQUEST");
            FriendshipStatus status = FriendshipStatus.valueOf(statusRequest);
            friendshipStatusMap.put(userFriendId, status);
        });
        return friendshipStatusMap;
    }

    @Override
    public List<Long> getCommonFriends(Long masterUser, Long userFriend) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sqlQuery = "SELECT f1.STATUS_USER_ID_2 AS common_friend_id "
                + "FROM FRIENDSHIP f1 "
                + "JOIN FRIENDSHIP f2 ON f1.STATUS_USER_ID_2 = f2.STATUS_USER_ID_2 "
                + "WHERE f1.STATUS_USER_ID_1 = :userId1 "
                + "AND f2.STATUS_USER_ID_1 = :userId2 "
                + "AND f1.STATUS_REQUEST = 'CONFIRMED' "
                + "AND f2.STATUS_REQUEST = 'CONFIRMED'";
        params.addValue("userId1", masterUser);
        params.addValue("userId2", userFriend);
        return jdbc.queryForList(sqlQuery, params, Long.class);
    }

}
