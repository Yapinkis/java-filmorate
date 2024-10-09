package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;

import java.util.Set;

interface GenreRepository {
    public void createGenre(Film film);

    public void deleteGenre(Film film);

    public Set<Genre> getGenre(Long id);

    public Genre getGenreById(Long id);

    public Set<Genre> getAllGenres();

}
