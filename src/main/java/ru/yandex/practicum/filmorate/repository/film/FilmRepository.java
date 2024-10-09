package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository {
    Film addFilm(Film film);

    Film update(Film film);

    Film get(Long id);

    List<Long> getAll();
}
