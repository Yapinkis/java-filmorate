package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.utility.CommonHelper;
import java.util.Set;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final FilmServiceImpl filmServiceImpl;
    private final CommonHelper commonHelper;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable Long id) {
        int minGenreValue = commonHelper.getMinGenreValue();
        int maxGenreValue = commonHelper.getMaxGenreValue();
        if (id < minGenreValue || id > maxGenreValue) {
            throw new EntityNotFoundException("Введённый " + id + " жанра фильма не обнаружен");
        }
        log.info("Жанр под идентификатором = {}", id);
        return filmServiceImpl.getGenre(id);
    }

    @GetMapping
    public Set<Genre> getGenres() {
        log.info("Список всех жанров:");
        return filmServiceImpl.getGenres();
    }
}
