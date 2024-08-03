package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ObjectFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmorateManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmorateManager filmorateManager;

    @Autowired
    public FilmController(FilmorateManager filmorateManager) {
        this.filmorateManager = filmorateManager;
    }

    private Map<Long,Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (!filmorateManager.validateFilm(film)) {
            return null;
        }
        film.setId(filmorateManager.nextFilmId());
        films.put(film.getId(), film);
        return film;
        //А тесты скорее всего не свалились из-за того,что я отлавливаю исключения assertThrows,
        // которые у меня выбрасываются в validateFilm
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film updatedFilm = films.get(film.getId());
        if (updatedFilm == null) {
            throw new ObjectFoundException("Данный фильм отсутствует");
        }
        filmorateManager.validateUpdateFilm(updatedFilm);
        updatedFilm.setName(film.getName());
        updatedFilm.setDescription(film.getDescription());
        updatedFilm.setDuration(film.getDuration());
        updatedFilm.setReleaseDate(film.getReleaseDate());
        films.put(updatedFilm.getId(), updatedFilm);
        return film;
    }

    @GetMapping
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
