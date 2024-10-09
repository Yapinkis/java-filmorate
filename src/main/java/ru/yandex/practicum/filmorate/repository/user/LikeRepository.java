package ru.yandex.practicum.filmorate.repository.user;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface LikeRepository {
    void addLike(Film film, User userLike);

    void removeLike(Film film, User userLike);

    Set<Long> getFilmLikes(Film film);

    List<Film> getMostPopularFilms(int count);
}
