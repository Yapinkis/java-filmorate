package ru.yandex.practicum.filmorate.repository.film;

import ru.yandex.practicum.filmorate.model.properties.MPA;

import java.util.List;

interface MPARepository {
    public MPA getMpaRating(Long id);

    public List<MPA> getAllMpaRatings();
}