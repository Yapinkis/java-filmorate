package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public List<Film> getFilms();

    public void addLike(Long usersId, Long filmsId);

    public void deleteLike(Long usersId, Long filmsId);

    public List<Film> getMostPopularFilms(int count);
}
