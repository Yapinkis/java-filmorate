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
import java.util.Optional;

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
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(usersId).orElseThrow(()
                -> new EntityNotFoundException("Пользователь не найден")));
        User user = userOptional.get();
        Film film = filmStorage.get(filmsId);
        if (film == null) {
            throw new EntityNotFoundException("Фильм не обнаружен");
            //Наверное проще проводить проверки на null или всё-таки лучше оборачивать объект в Optional?
        }
        filmStorage.addLike(film,user);
    }

    @Override
    public void deleteLike(Long usersId, Long filmsId) {
        Optional<User> userOptional = Optional.ofNullable(userStorage.get(usersId).orElseThrow(()
                -> new EntityNotFoundException("Пользователь не найден")));
        User user = userOptional.get();
        Film film = filmStorage.get(filmsId);
        if (film == null) {
            throw new EntityNotFoundException("Фильм не обнаружен");
            //У меня большое количество кода дублируется от метода к методу, он абсолютно одинаковый. Могу ли я
            // в FilmService сделать меод получения значений User, Film и реализовать его в сервисе?
            // Укладывется ли это в логику REST-архитектуры?
        }
        filmStorage.removeLike(film,user);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return new ArrayList<>(filmStorage.getMostPopularFilms(count));
    }

    private void validate(Film film) {
        final LocalDate dataFlag = LocalDate.of(1895,12,28);
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не соответствует заданным парметрам");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма превышает длинну в 200 символов");
        }
        if (film.getReleaseDate().isBefore(dataFlag)) {
            throw new ValidationException("Дата релиза фильма установлена до 1985 года");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма имеет отрицательное значение");
        }
        //Учитывая аннотации на объектах, метод валидации нужен скорее для обновления объекта, просто что
        // бы не дублировать код для метода add и update я решил оставить один общий метод. И соответсвует
        // ли это REST-архитектуре или у нас в сервисе должны быть только переопределённые методы интерфесов
    }
}
