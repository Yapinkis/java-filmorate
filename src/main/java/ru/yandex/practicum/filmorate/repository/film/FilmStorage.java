package ru.yandex.practicum.filmorate.repository.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmStorage implements FilmRepository {
    private Long filmId = 0L;
    Map<Long, Film> films = new HashMap<>();
    Map<Long, Set<Long>> filmsLikes = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        films.put(film.getId(),film);
        return film;
    }
    @Override
    public Long generateId() {
        return ++filmId;
    }
    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    @Override
    public void addLike(Film film, User userLike) {
        Set<Long> findUserLikes = filmsLikes.computeIfAbsent(film.getId(), k -> new HashSet<>());
        findUserLikes.add(userLike.getId());
    }
    @Override
    public void removeLike(Film film, User userLike) {
        Set<Long> likes = filmsLikes.get(film.getId());
        likes.remove(userLike.getId());
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        return films.values().stream().sorted((f1, f2) -> Integer.compare(filmsLikes.getOrDefault(f2.getId(),
                Collections.emptySet()).size(), filmsLikes.getOrDefault(f1.getId(),
                Collections.emptySet()).size())).limit(count).collect(Collectors.toList());
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
