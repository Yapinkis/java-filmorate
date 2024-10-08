package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    public User addUser(User user);

    public User updateUser(User user);

    public List<User> getUsers();

    public User get(Long userId);

    public void addFriend(Long user, Long userFriend);

    public void deleteFriend(Long user, Long userFriend);

    public List<User> getMutualFriends(Long user, Long userFriend);

    public List<User> getFriends(Long user);
}
