package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.FriendshipStatus;

import java.util.List;
import java.util.Map;

public interface FriendShipRepository {
    void addFriend(User masterUser, User userFriend);

    void removeFriend(User masterUser, User userFriend);

    Map<Long, FriendshipStatus> getFriendshipStatus(Long user);

    List<User> getCommonFriends(Long masterUser, Long userFriend);
}
