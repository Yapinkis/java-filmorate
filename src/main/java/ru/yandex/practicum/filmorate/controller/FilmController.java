package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        log.info("Добавлен фильм ={}", film.getName());
        filmServiceImpl.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        log.info("Обновление фильма ={}", film.getName());
        filmServiceImpl.updateFilm(film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Список всех фильмов");
        return filmServiceImpl.getFilms();
    }

    @PutMapping("/{filmId}/like/{id}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long id) {
        log.info("Фильму {}, добавлен лайк от пользователя {}", filmId, id);
        filmServiceImpl.addLike(filmId, id);
    }

    @DeleteMapping("/{filmId}/like/{id}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long id) {
        log.info("Пользователь с идентификатором={}, удаляет лайк фильму={}", id, filmId);
        filmServiceImpl.deleteLike(id,filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Количество самых популярных фильмов равно={}", count);
        return filmServiceImpl.getMostPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Фильм под идентификтаором={}", id);
        return filmServiceImpl.getFilmById(id);
    }

}
