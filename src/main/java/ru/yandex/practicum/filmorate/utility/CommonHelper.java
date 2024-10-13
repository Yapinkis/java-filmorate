package ru.yandex.practicum.filmorate.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.Genre;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class CommonHelper {
    private final NamedParameterJdbcOperations jdbc;
    @Getter @Setter
    private static Long usersCount = 0L;
    @Getter @Setter
    private static Long filmsCount = 0L;

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

    public static void checkRow(int count) {
        if (count == 0) {
            throw new EntityNotFoundException("Фильм не найден");
        }
    }

    public static void checkCondition(Film film, User user) {
        if (film == null) {
            throw new EntityNotFoundException("При попытке обратиться к объету Film возникла ошибка, объект не обнаружен");
        }
        if (user == null) {
            throw new EntityNotFoundException("При попытке обратиться к объету User возникла ошибка, объект не обнаружен");
        }
    }

    public static void checkCondition(User user, User userFriend) {
        if (user == null) {
            throw new EntityNotFoundException("При попытке обратиться к объету User возникла ошибка, объект не обнаружен");
        }
        if (userFriend == null) {
            throw new EntityNotFoundException("При попытке обратиться к объету UserFriend возникла ошибка, объект не обнаружен");
        }
    }

    public void checkGenreId(Genre genre) {
        if (genre.getId() > getMaxGenreValue() || genre.getId() < getMinGenreValue()) {
            throw new ValidationException("Введён непостуимый идентификатор жанра: " + genre.getId());
        }
    }

    public int getMinMpaTableValue() {
        String minMpaValue = "SELECT MIN(RATING_ID) FROM MPA";
        return jdbc.queryForObject(minMpaValue, new MapSqlParameterSource(), Integer.class);
    }

    public int getMaxMpaTableValue() {
        String minMpaValue = "SELECT MAX(RATING_ID) FROM MPA";
        return jdbc.queryForObject(minMpaValue, new MapSqlParameterSource(), Integer.class);
    }

    public int getMinGenreValue() {
        String minGenreValue = "SELECT MIN(GENRE_ID) FROM GENRE";
        return jdbc.queryForObject(minGenreValue, new MapSqlParameterSource(), Integer.class);
    }

    public int getMaxGenreValue() {
        String maxGenreValue = "SELECT MAX(GENRE_ID) FROM GENRE";
        return jdbc.queryForObject(maxGenreValue, new MapSqlParameterSource(), Integer.class);
    }


}
