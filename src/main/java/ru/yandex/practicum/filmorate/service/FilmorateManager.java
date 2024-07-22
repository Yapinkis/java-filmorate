package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class FilmorateManager implements MappingManager {
    private int filmId = 0;
    private int userId = 0;

    @Override
    public boolean validateFilm(Film film) {
        final LocalDate dataFlag = LocalDate.of(1895,12,28);
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не соответствует заданным парметрам");
        }
        if (film.getDescription().length() > 200) {
            log.error("Описание фильма не может превышать 200 символов: {}", film.getDescription().length());
            throw new ValidationException("Описание фильма превышает длинну в 200 символов");
        }
        if (film.getReleaseDate().isBefore(dataFlag)) {
            log.error("Дата релиза фильма не может быть раньше 28 декабря 1985 года: {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза фильма установлена до 1985 года");
        }
        if (film.getDuration() < 0) {
            log.error("Продолжительность фильма не должна иметь отрицательное значение: {}", film.getReleaseDate());
            throw new ValidationException("Продолжительность фильма имеет отрицательное значение");
        }
        return true;
    }

    @Override
    public boolean validateUpdateFilm(Film film) {
        if (film.getId() == null) {
            throw new UpdateException("Фильм с данным Id не найден");
        }
        validateFilm(film);
        return true;
    }

    @Override
    public boolean validateUser(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.error("Email пользователя не может быть пустым и должен содержать символ '@': {}", user.getEmail());
            throw new ValidationException("Email пользователя не соответствует установленным параметрам");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            log.error("Логин пользователя не может быть пустым или содержать пробелы: {}", user.getLogin());
            throw new ValidationException("Логин пользователя не соответствует установленным параметрам");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения пользователя не может быть в будущем: {}", user.getBirthday());
            throw new ValidationException("Дата рождения пользователя не соответствует установленным параметрам");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.error("Имя для отображения отсутствует. Будет использован логин: {}", user.getLogin());
            user.setName(user.getLogin());
        }
        return true;
    }

    @Override
    public boolean validateUpdateUser(User user) {
        if (user.getId() == null) {
            throw new UpdateException("Пользователь с данным Id не найден");
        }
        validateUser(user);
        return true;
    }

    @Override
    public int nextFilmId() {
        return ++filmId;
    }

    @Override
    public int nextUserId() {
        return ++userId;
    }


}
