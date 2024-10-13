package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MPA;

import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Positive
    private Long duration;
    private Set<Long> likes;
    private Set<Genre> genres;
    private MPA mpa;
}