package ru.yandex.practicum.filmorate.utility;

import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.Genre;

import java.time.LocalDate;

public class CommonHelper {
    @Getter @Setter
    private static Long usersCount = 0L;
    @Getter @Setter
    private static Long filmsCount = 0L;
    public static final int minGenreCount = 1;
    public static final int maxGenreCount = 6;

    public static void validateFilm(Film film) {
        final LocalDate dataFlag = LocalDate.of(1895,12,28);
        if (film == null) {
            throw new EntityNotFoundException("Фильм не обнаружен");
        }
        if (film.getReleaseDate().isBefore(dataFlag)) {
            throw new ValidationException("Дата релиза фильма установлена до 1985 года");
        }
        if (film.getMpa().getId() > 5 || film.getMpa().getId() < 1) {
            throw new ValidationException("Id рейтинга MPA не соответствует допустимому диапозону от 1 до 5 и равен "
                    + film.getMpa().getId());
        }
    }

    public static void validateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    public static void validateUserId(Long id) {
        if (id > usersCount) {
            throw new EntityNotFoundException("User Id = " + id + " больше максимального значения в БД = " + usersCount);
        }
    }

    public static void validateFilmId(Long id) {
        if (id > filmsCount) {
            throw new EntityNotFoundException("Film Id = " + id + " больше максимального значения в БД = " + filmsCount);
        }
    }

    public static void checkGenreId(Genre genre) {
        if (genre.getId() > maxGenreCount || genre.getId() < minGenreCount) {
            throw new ValidationException("Введён непостуимый идентификатор жанра: " + genre.getId());
        }
    }

    public static void checkRow(int count) {
        if (count == 0) {
            throw new EntityNotFoundException("Фильм не найден");
        }
    }

    public static void checkCondition(Film film, User user) {
        if (film == null || user == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
    }

    public static void checkCondition(User user, User userFriend) {
        if (user == null || userFriend == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
    }

}
