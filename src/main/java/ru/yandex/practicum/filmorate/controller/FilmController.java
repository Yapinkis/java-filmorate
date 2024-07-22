package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmorateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmorateManager filmorateManager = new FilmorateManager();
    // Создаются ли для контроллеров интерфейсы? Просто я бы мог создать три метода: add, update и get и уже их
    // реализовать в кажом контроллере или это плохая практика?
    // upd.Посмотрел запись вебинара и как я понял мы подобные штуки в будущем будем строить исключительно на аннотациях
    // Jakarta Bean Validation наверное то же имеет смысл импортировать в проект

    private Map<Integer,Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (filmorateManager.validateFilm(film)) {
            film.setId(filmorateManager.nextFilmId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            Film updatedFilm = films.get(film.getId());
            updatedFilm.setName(film.getName());
            updatedFilm.setDescription(film.getDescription());
            updatedFilm.setDuration(film.getDuration());
            updatedFilm.setReleaseDate(film.getReleaseDate());
            filmorateManager.validateUpdateFilm(updatedFilm);
            films.put(updatedFilm.getId(), updatedFilm);
        } catch (UpdateException e) {
            log.error("Фильм не найден: {}",e.getMessage());
        }
        return film;
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
