package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

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
        User user = userStorage.get(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        return user;
    }

    @Override
    public void addFriend(Long user, Long userFriend) {
        User masterUser = userStorage.get(user);
        User userNewFriend = userStorage.get(userFriend);
        //Я решил в итоге оставить проверку на null и избавиться от Optional,
        // кажется в данном случае это действительно излишне
        if (masterUser == null || userNewFriend == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
        userStorage.addFriend(masterUser,userNewFriend);
    }

    @Override
    public void deleteFriend(Long user, Long userFriend) {
        User masterUser = userStorage.get(user);
        User userNewFriend = userStorage.get(userFriend);
        if (masterUser == null || userNewFriend == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
        userStorage.deleteFriend(masterUser,userNewFriend);
    }

    @Override
    public List<User> getMutualFriends(Long user, Long userFriend) {
        User masterUser = userStorage.get(user);
        User userNewFriend = userStorage.get(userFriend);
        if (masterUser == null || userNewFriend == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
        return userStorage.getMutualFriends(masterUser, userNewFriend);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = userStorage.get(userId);
        if (user == null) {
            throw new EntityNotFoundException("Пользователь с ID " + userId + " не найден.");
        }
        return userStorage.getFriends(user);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
    /*
    Почитал про маркерные интерфейсы, очень удобная штука. Но я думаю в моём случае она не очень актуальна т.к.
    мне необходимо отслеживать валидацию полей объектов как при создании, так и при обновлении, а аннотация @Valid
    оказывется с эти отлично справляется. Утилитарный класс как таковой тоже нет особого смысла делать т.к.
    после рефакторинга такой метод очень хорошо читается и вполне укладывается в бизнес-логику)
     */

}
