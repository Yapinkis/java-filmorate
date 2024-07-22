package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


public interface MappingManager {
    boolean validateFilm(Film film);
    //имеет ли смысл делать обобщенные методы по типу <T> boolean validate(T object)
    //а далее в реализации через instanceof производить каст объекта? Или лучше сразу написать несколько реализаций
    //схожих методов, как мы это делали с тасками-сабтасками-эпиками

    boolean validateUpdateFilm(Film film);

    boolean validateUser(User user);

    boolean validateUpdateUser(User user);

    int nextFilmId();

    int nextUserId();
}
