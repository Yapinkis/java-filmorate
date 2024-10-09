package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserRepository {
    User addUser(User user);

    User update(User user);

    User get(Long id);

    List<Long> getAll();
}
