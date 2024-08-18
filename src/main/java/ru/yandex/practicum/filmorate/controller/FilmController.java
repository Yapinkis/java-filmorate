package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmServiceImpl filmServiceImpl;

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film){
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
    @PutMapping("/{id}/like/{userId}")
    public void makeLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с идентификатором={}, сьавит лайк фильму={}", userId, id);
        filmServiceImpl.addLike(userId,id);
    }
    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с идентификатором={}, удаляет лайк фильму={}", userId, id);
        filmServiceImpl.deleteLike(userId,id);
    }
    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false, defaultValue = "10") int count) {
        log.info("Количество самых популярных фильмов равно={}", count);
        return filmServiceImpl.getMostPopularFilms(count);
    }

}
