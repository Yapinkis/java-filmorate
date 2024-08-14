package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmorateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final FilmorateManager filmorateManager;

    @Autowired
    public UserController(FilmorateManager filmorateManager) {
        this.filmorateManager = filmorateManager;
    }

    private Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (!filmorateManager.validateUser(user)) {
            return null;
        }
        user.setId(filmorateManager.nextUserId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User updatedUser = users.get(user.getId());
        if (updatedUser == null) {
            throw new ObjectFoundException("Указанный User не обнаружен");
        }
        filmorateManager.validateUpdateUser(updatedUser);
        updatedUser.setLogin(user.getLogin());
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setBirthday(user.getBirthday());
        users.put(updatedUser.getId(), updatedUser);
        return user;
    }

    @GetMapping
    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
