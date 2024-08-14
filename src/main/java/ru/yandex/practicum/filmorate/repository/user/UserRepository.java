package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public User addUser(User user);

    Long generateId();

    Optional<User> get(Long id);

    void addFriend(User user, User userFriend);

    void deleteFriend(User user, User userFriend);

    List<User> getMutualFriends(User user, User userFriend);

    List<User> getFriends(User user);
}
