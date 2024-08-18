package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserServiceImpl userServiceImpl;
    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        log.info("Создан пользователь={}", user.getName());
        userServiceImpl.addUser(user);
        return user;
    }
    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Пользователь обновлён={}", user.getName());
        userServiceImpl.updateUser(user);
        return user;
    }
    @GetMapping
    public List<User> getUsers() {
        log.info("Список всех юзеров");
        return userServiceImpl.getUsers();
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.info("Найти пользователя с идентификатором={}", id);
        return userServiceImpl.get(id);
    }
    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Добавить пользователя с идентификатором={}, в друзья к ={}", id, friendId);
        userServiceImpl.addFriend(id,friendId);
    }
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Пользователя с идентификатором={}, удалён из друзей у пользователя ={}", id, friendId);
        userServiceImpl.deleteFriend(id,friendId);
    }
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Список друзей пользователя с идентификатором ={}", id);
        return userServiceImpl.getFriends(id);
    }
    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Список друзей пользователя ={} с пользователем ={}", id, otherId);
        return userServiceImpl.getMutualFriends(id, otherId);
    }
}
