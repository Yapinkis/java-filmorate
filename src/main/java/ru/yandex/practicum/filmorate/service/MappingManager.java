package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


public interface MappingManager {
    boolean validateFilm(Film film);

    boolean validateUpdateFilm(Film film);

    boolean validateUser(User user);

    boolean validateUpdateUser(User user);

    Long nextFilmId();

    Long nextUserId();
}
