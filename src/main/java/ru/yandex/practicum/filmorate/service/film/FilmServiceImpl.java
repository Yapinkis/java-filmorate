package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.film.FilmStorage;
import ru.yandex.practicum.filmorate.repository.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Override
    public Film addFilm(Film film) {
        validate(film);
        film.setId(filmStorage.generateId());
        filmStorage.addFilm(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        Film updatedFilm = filmStorage.get(film.getId());
        if (updatedFilm == null) {
            throw new EntityNotFoundException("Фильм с указанным Id не обнаружен");
        }
        updatedFilm.setName(film.getName());
        updatedFilm.setDescription(film.getDescription());
        updatedFilm.setReleaseDate(film.getReleaseDate());
        updatedFilm.setDuration(film.getDuration());
        filmStorage.addFilm(updatedFilm);
        return updatedFilm;
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public void addLike(Long usersId, Long filmsId) {
        User user = userStorage.get(usersId);
        Film film = filmStorage.get(filmsId);
        if (film == null || user == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
        filmStorage.addLike(film,user);
    }

    @Override
    public void deleteLike(Long usersId, Long filmsId) {
        User user = userStorage.get(usersId);
        Film film = filmStorage.get(filmsId);
        if (film == null || user == null) {
            throw new EntityNotFoundException("Ошибка, объект не обнаружен");
        }
        filmStorage.removeLike(film,user);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return new ArrayList<>(filmStorage.getMostPopularFilms(count));
    }

    private void validate(Film film) {
        final LocalDate dataFlag = LocalDate.of(1895,12,28);
        if (film.getReleaseDate().isBefore(dataFlag)) {
            throw new ValidationException("Дата релиза фильма установлена до 1985 года");
        }
    }

}
