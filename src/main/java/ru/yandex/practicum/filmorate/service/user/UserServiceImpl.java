package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.FriendshipStatus;
import ru.yandex.practicum.filmorate.repository.user.JdbcFriendshipStorage;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserStorage;
import ru.yandex.practicum.filmorate.utility.CommonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.utility.CommonHelper.checkCondition;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JdbcFriendshipStorage jdbcFriendshipStorage;
    private final JdbcUserStorage jdbcUserStorage;

    @Override
    public User addUser(User user) {
        CommonHelper.validateUser(user);
        User created = jdbcUserStorage.addUser(user);
        created.setFriendships(jdbcFriendshipStorage.getFriendshipStatus(created.getId()));
        return created;
    }

    @Override
    public User updateUser(User user) {
        CommonHelper.validateUser(user);
        User userUpdate = jdbcUserStorage.update(user);
        userUpdate.setFriendships(jdbcFriendshipStorage.getFriendshipStatus(userUpdate.getId()));
        return userUpdate;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(jdbcUserStorage.getAll());
    }

    @Override
    public void addFriend(Long user, Long userFriend) {
        CommonHelper.validateUserId(user);
        CommonHelper.validateUserId(userFriend);
        User masterUser = jdbcUserStorage.get(user);
        User userNewFriend = jdbcUserStorage.get(userFriend);
        checkCondition(masterUser,userNewFriend);
        jdbcFriendshipStorage.addFriend(masterUser,userNewFriend);
        // masterUser.setFriendships(jdbcFriendshipStorage.getFriendshipStatus(user));
        // Да эта строк как в addFriend так и в deleteFriend лишние т.к. это было бы актуально для установки статуса
        // дружбы для конкретного объекта, а не для выболнения операции добавления\удаления из друзей
    }

    @Override
    public void deleteFriend(Long user, Long userFriend) {
        CommonHelper.validateUserId(user);
        CommonHelper.validateUserId(userFriend);
        User masterUser = jdbcUserStorage.get(user);
        User userNewFriend = jdbcUserStorage.get(userFriend);
        checkCondition(masterUser,userNewFriend);
        jdbcFriendshipStorage.removeFriend(masterUser,userNewFriend);
    }

    @Override
    public List<User> getMutualFriends(Long user, Long userFriend) {
        CommonHelper.validateUserId(user);
        CommonHelper.validateUserId(userFriend);
        return new ArrayList<>(jdbcFriendshipStorage.getCommonFriends(user, userFriend));
    }

    @Override
    public List<User> getFriends(Long userId) {
        CommonHelper.validateUserId(userId);
        List<User> userFriends = new ArrayList<>();
        User user = jdbcUserStorage.get(userId);
        user.setFriendships(jdbcFriendshipStorage.getFriendshipStatus(userId));
        for (Map.Entry<Long,FriendshipStatus> entry : user.getFriendships().entrySet()) {
            Long userFriendId = entry.getKey();
            User userFriend = jdbcUserStorage.get(userFriendId);
            userFriends.add(userFriend);
        }
        return userFriends;
    }
}
