package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService1 {
    // Почему-то при названии интерфейса UserService - возникает ошибка
    // Cannot access ru.yandex.practicum.filmorate.service.user.UserService

    private final UserStorage userStorage;

    @Override
    public User addUser(User user) {
        validate(user);
        user.setId(userStorage.generateId());
        userStorage.addUser(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        User userUpdate = get(user.getId());
        userUpdate.setName(user.getName());
        userUpdate.setLogin(user.getLogin());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setBirthday(user.getBirthday());
        userStorage.addUser(userUpdate);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @Override
    public User get(Long userId) {
        return userStorage.get(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь" + userId + " не найден"));
    }

    @Override
    public void addFriend(Long user, Long userFriend) {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(user).orElseThrow(()
                -> new EntityNotFoundException("Пользователь user не найден")));
        User masterUser = userOptional.get();
        Optional<User> userFriendOptional = Optional.ofNullable(userStorage.get(userFriend).orElseThrow(()
                -> new EntityNotFoundException("Пользователь userFriend не найден")));
        User userNewFriend = userFriendOptional.get();
        //аналогично по комментарию в FilmServiceImpl
        userStorage.addFriend(masterUser,userNewFriend);
    }

    @Override
    public void deleteFriend(Long user, Long userFriend) {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(user).orElseThrow(()
                -> new EntityNotFoundException("Пользователь user не найден")));
        User masterUser = userOptional.get();
        Optional<User> userFriendOptional = Optional.ofNullable(userStorage.get(userFriend).orElseThrow(()
                -> new EntityNotFoundException("Пользователь userFriend не найден")));
        User userNewFriend = userFriendOptional.get();
        userStorage.deleteFriend(masterUser,userNewFriend);
    }

    @Override
    public List<User> getMutualFriends(Long user, Long userFriend) {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(user).orElseThrow(()
                -> new EntityNotFoundException("Пользователь user не найден")));
        User masterUser = userOptional.get();
        Optional<User> userFriendOptional = Optional.ofNullable(userStorage.get(userFriend).orElseThrow(()
                -> new EntityNotFoundException("Пользователь userFriend не найден")));
        User userNewFriend = userFriendOptional.get();
        return userStorage.getMutualFriends(masterUser, userNewFriend);
    }

    @Override
    public List<User> getFriends(Long user) {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(user).orElseThrow(()
                -> new EntityNotFoundException("Пользователь user не найден")));
        User userGet = userOptional.get();
        return userStorage.getFriends(userGet);
    }

    private void validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email пользователя не соответствует установленным параметрам");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин пользователя не соответствует установленным параметрам");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения пользователя не соответствует установленным параметрам");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
