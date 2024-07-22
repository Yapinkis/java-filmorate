package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmorateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    FilmorateManager filmorateManager = new FilmorateManager();
    private Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@RequestBody User user) {
        if (filmorateManager.validateUser(user)) {
            user.setId(filmorateManager.nextUserId());
            users.put(user.getId(), user);
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            User updatedUser = users.get(user.getId());
            updatedUser.setLogin(user.getLogin());
            updatedUser.setName(user.getName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setBirthday(user.getBirthday());
            filmorateManager.validateUpdateUser(updatedUser);
            users.put(updatedUser.getId(), updatedUser);
        } catch (UpdateException e) {
            log.error("Username не найден: {}",e.getMessage());
        }
        return user;
    }

    @GetMapping
    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
