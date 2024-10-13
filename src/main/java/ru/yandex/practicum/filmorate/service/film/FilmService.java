package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MPA;

import java.util.List;
import java.util.Set;

interface FilmService {
    public Film addFilm(Film film);

    public Film updateFilm(Film film);

    public List<Film> getFilms();

    public Film getFilmById(long id);

    public void addLike(Long usersId, Long filmsId);

    public void deleteLike(Long usersId, Long filmsId);

    public List<Film> getMostPopularFilms(int count);

    public Set<Genre> getGenres();

    public Genre getGenre(Long genreId);

    public List<MPA> getAllRatings();

    public MPA getRating(Long ratingId);
}
