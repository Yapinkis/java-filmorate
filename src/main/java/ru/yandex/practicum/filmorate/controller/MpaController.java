package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final FilmServiceImpl filmServiceImpl;

    @GetMapping("/{id}")
    public MPA getRatingById(@Min(1) @Max(5) @PathVariable Long id) {
        log.info("MPA-рейтинг под идентификатором = {}", id);
        return filmServiceImpl.getRating(id);
    }

    @GetMapping
    public List<MPA> getAllRatings() {
        log.info("Список всех MPA-рейтингов");
        return filmServiceImpl.getAllRatings();
    }
}
