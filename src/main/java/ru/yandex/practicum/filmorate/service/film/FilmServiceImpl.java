package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.repository.film.JbdcGenreStorage;
import ru.yandex.practicum.filmorate.repository.film.JbdcMpaStorage;
import ru.yandex.practicum.filmorate.repository.film.JdbsFilmStorage;
import ru.yandex.practicum.filmorate.repository.user.JbdcLikeStorage;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserStorage;
import ru.yandex.practicum.filmorate.utility.CommonHelper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final JdbsFilmStorage jdbsFilmStorage;
    private final JdbcUserStorage jdbcUserStorage;
    private final JbdcGenreStorage jbdcGenreStorage;
    private final JbdcMpaStorage jbdcMpaStorage;
    private final JbdcLikeStorage jbdcLikeStorage;

    @Override
    public Film addFilm(Film film) {
        CommonHelper.validateFilm(film);
        Film created = jdbsFilmStorage.addFilm(film);
        jbdcGenreStorage.createGenre(created);
        created.setGenres(jbdcGenreStorage.getGenre(film.getId()));
        created.setMpa(jbdcMpaStorage.getMpaRating(film.getMpa().getId()));
        return created;
    }

    @Override
    public Film updateFilm(Film film) {
        CommonHelper.validateFilm(film);
        Film updatedFilm = jdbsFilmStorage.update(film);
        jbdcGenreStorage.deleteGenre(updatedFilm);
        updatedFilm.setGenres(jbdcGenreStorage.getGenre(film.getId()));
        updatedFilm.setMpa(jbdcMpaStorage.getMpaRating(film.getMpa().getId()));
        updatedFilm.setLikes(jbdcLikeStorage.getFilmLikes(film));
        return updatedFilm;
    }

    @Override
    public List<Film> getFilms() {
        ArrayList<Long> filmsIds = new ArrayList<>(jdbsFilmStorage.getAll());
        ArrayList<Film> films = new ArrayList<>();
        for (Long id : filmsIds) {
            Film film = jdbsFilmStorage.get(id);
            films.add(film);
        }
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        Film film = jdbsFilmStorage.get(id);
        film.setGenres(jbdcGenreStorage.getGenre(film.getId()));
        film.setMpa(jbdcMpaStorage.getMpaRating(film.getMpa().getId()));
        return film;
    }

    @Override
    public void addLike(Long filmsId, Long usersId) {
        Film film = jdbsFilmStorage.get(filmsId);
        User user = jdbcUserStorage.get(usersId);
        CommonHelper.checkCondition(film,user);
        jbdcLikeStorage.addLike(film, user);
    }

    @Override
    public void deleteLike(Long usersId, Long filmsId) {
        User user = jdbcUserStorage.get(usersId);
        Film film = jdbsFilmStorage.get(filmsId);
        CommonHelper.checkCondition(film,user);
        CommonHelper.validateFilm(film);
        CommonHelper.validateUser(user);
        film.setGenres(jbdcGenreStorage.getGenre(film.getId()));
        film.setMpa(jbdcMpaStorage.getMpaRating(film.getMpa().getId()));
        jbdcLikeStorage.removeLike(film,user);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return new ArrayList<>(jbdcLikeStorage.getMostPopularFilms(count));
    }

    @Override
    public List<MPA> getAllRatings() {
        return jbdcMpaStorage.getAllMpaRatings();
    }

    @Override
    public MPA getRating(Long ratingId) {
        return jbdcMpaStorage.getMpaRating(ratingId);
    }

    @Override
    public LinkedHashSet<Genre> getGenres() {
        return new LinkedHashSet<>(jbdcGenreStorage.getAllGenres());
    }

    @Override
    public Genre getGenre(Long genreId) {
        return jbdcGenreStorage.getGenreById(genreId);
    }

}
