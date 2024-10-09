package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import java.util.LinkedHashSet;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final FilmServiceImpl filmServiceImpl;

    @GetMapping("/{id}")
    public Genre getGenreById(@Min(1) @Max(6) @PathVariable Long id) {
        log.info("Жанр под идентификатором = {}", id);
        return filmServiceImpl.getGenre(id);
    }

    @GetMapping
    public LinkedHashSet<Genre> getGenres() {
        log.info("Список всех жанров:");
        return filmServiceImpl.getGenres();
    }
}
