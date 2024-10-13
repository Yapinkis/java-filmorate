package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.service.film.FilmServiceImpl;
import ru.yandex.practicum.filmorate.utility.CommonHelper;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final FilmServiceImpl filmServiceImpl;
    private final CommonHelper commonHelper;

    @GetMapping("/{id}")
    public MPA getRatingById(@PathVariable Long id) {
        int minMpaValue = commonHelper.getMinMpaTableValue();
        int maxMpaValue = commonHelper.getMaxMpaTableValue();
        if (id < minMpaValue || id > maxMpaValue) {
            throw new EntityNotFoundException("Введённый " + id + " жанра-MPA не обнаружен");
        }
        // есть такой варинат с динамическим получением ограничений по жанрам. Я просто думал, что нам лучше
        // использовать аннотации, а туда можно засунуть только static значение, что невозможно при работе
        // с NamedParameterJdbcOperations или я просто не нашёл способа как это сделать....
        log.info("MPA-рейтинг под идентификатором = {}", id);
        return filmServiceImpl.getRating(id);
    }

    @GetMapping
    public List<MPA> getAllRatings() {
        log.info("Список всех MPA-рейтингов");
        return filmServiceImpl.getAllRatings();
    }
}
