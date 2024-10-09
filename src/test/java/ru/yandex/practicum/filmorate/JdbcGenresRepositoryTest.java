package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.repository.film.JbdcGenreStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JdbcTest
@Import(JbdcGenreStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JbdcGenreStorage")
public class JdbcGenresRepositoryTest {
    public static final long FILM_ID_ONE = 1L;
    public static final long FILM_ID_TWO = 2L;
    public static final long FILM_ID_THREE = 3L;

    private final JbdcGenreStorage jbdcGenreStorage;

    static Film createFilm(Long Id, String name, String description, Long duration, LocalDate releaseDate, MPA mpa, LinkedHashSet<Genre> genre) {
        Film film = new Film();
        film.setId(Id);
        film.setName(name);
        film.setDescription(description);
        film.setDuration(duration);
        film.setReleaseDate(releaseDate);
        film.setMpa(mpa);
        film.setGenres(genre);
        return film;
    }

    static List<Film> getTestFilms() {
        return List.of(
                createFilm(FILM_ID_ONE,"One_Film","One_Description",110L,
                        LocalDate.of(1999,12,11),new MPA(FILM_ID_ONE, "G"),
                        new LinkedHashSet<>(List.of(new Genre(1L,"Комедия")))),
                createFilm(FILM_ID_TWO,"Two_Film","Two_Description",120L,
                        LocalDate.of(1995,10,4),new MPA(FILM_ID_TWO, "PG"),
                        new LinkedHashSet<>(List.of(new Genre(1L,"Комедия"), new Genre(2L,"Драма")))),
                createFilm(FILM_ID_THREE,"Three_Film","Three_Description",130L,
                        LocalDate.of(2024,1,22),new MPA(FILM_ID_THREE, "PG-13"),
                        new LinkedHashSet<>(List.of(new Genre(1L,"Комедия"),
                                new Genre(2L,"Драма"), new Genre(3L,"Мультфильм")))));
    }

    static Film getFilm(Long num) {
        return getTestFilms().stream().filter(film -> film.getId() == num).findFirst().orElseThrow(()
                -> new EntityNotFoundException("Film не найден"));
    }

    @Test
    @DisplayName("Заполняет таблицу FILM_GENRE на основе информации объекта Film")
    public void createGenreMethod_should_create_values_for_FILM_GENRE() {
        Film film = getFilm(FILM_ID_THREE);
        jbdcGenreStorage.createGenre(film);
        LinkedHashSet<Genre> fromGenre = jbdcGenreStorage.getGenre(film.getId());
        assertThat(film.getGenres()).isEqualTo(fromGenre);
    }

    @Test
    @DisplayName("Возвращает Set параметра Genre объекта Film в порядке добавления")
    public void getGenreMethod_should_return_Set_0f_Genre_in_order_where_added() {
        Film film = getFilm(FILM_ID_ONE);
        jbdcGenreStorage.deleteGenre(film);
        film.setGenres(new LinkedHashSet<>(List.of(new Genre(3L,"Мультфильм"),
                new Genre(1L,"Комедия"),new Genre(2L,"Драма"))));
        jbdcGenreStorage.createGenre(film);
        film.getGenres().add(new Genre(4L,"Триллер"));
        jbdcGenreStorage.createGenre(film);
        LinkedHashSet<Genre> fromGenre = jbdcGenreStorage.getGenre(film.getId());
        LinkedHashSet<Long> genresId = film.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        assertThat(fromGenre.stream().map(Genre::getId).collect(Collectors.toList()))
                .isEqualTo(new ArrayList<>(genresId));
    }

    @Test
    @DisplayName("Возвращает список всех имеющихся жанров из таблицы GENRE")
    public void getAllGenresMethod_should_return_Set_of_all_Genres_from_GENRE() {
        Film film = getFilm(FILM_ID_THREE);
        Set<Genre> genres = jbdcGenreStorage.getAllGenres();
        assertTrue(genres.stream().anyMatch(genre -> genre.getName().equals("Комедия")));
        assertTrue(genres.stream().anyMatch(genre -> genre.getName().equals("Драма")));
        assertTrue(genres.stream().anyMatch(genre -> genre.getName().equals("Мультфильм")));
        assertTrue(genres.stream().anyMatch(genre -> genre.getName().equals("Триллер")));
    }

}
