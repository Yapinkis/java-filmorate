package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmRepository {
    public Film addFilm(Film film);

    Long generateId();

    Film get(Long id);

    void addLike(Film film, User userLike);

    void removeLike(Film film, User userLike);

    List<Film> getMostPopularFilms(int count);
}
