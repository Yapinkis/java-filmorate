package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.repository.film.JbdcMpaStorage;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@JdbcTest
@Import(JbdcMpaStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcMpaStorage")
public class JdbcMPARepositoryTest {
    public static final long FILM_ID_ONE = 1L;
    public static final long FILM_ID_TWO = 2L;
    public static final long FILM_ID_THREE = 3L;

    private final JbdcMpaStorage jbdcMpaStorage;

    static Film createFilm(Long Id, String name, String description, Long duration, LocalDate releaseDate, MPA mpa) {
        Film film = new Film();
        film.setId(Id);
        film.setName(name);
        film.setDescription(description);
        film.setDuration(duration);
        film.setReleaseDate(releaseDate);
        film.setMpa(mpa);
        return film;
    }

    static List<Film> getTestFilms() {
        return List.of(
                createFilm(FILM_ID_ONE,"One_Film","One_Description",110L,
                        LocalDate.of(1999,12,11),new MPA(FILM_ID_ONE, "G")),
                createFilm(FILM_ID_TWO,"Two_Film","Two_Description",120L,
                        LocalDate.of(1995,10,4),new MPA(FILM_ID_TWO, "PG")),
                createFilm(FILM_ID_THREE,"Three_Film","Three_Description",130L,
                        LocalDate.of(2024,1,22),new MPA(FILM_ID_THREE, "PG-13")));
    }

    @Test
    @DisplayName("Возвращает эксземпляр MPA_рейтинга для объекта Film")
    public void getAllRatingsMethod_should_return_List_of_all_MPA() {
        List<MPA> fromFilmsMPAs = getTestFilms().stream().map(Film::getMpa).toList();
        List<MPA> MPAs = new ArrayList<>(jbdcMpaStorage.getAllMpaRatings());
        assertThat(MPAs).usingRecursiveComparison().isEqualTo(fromFilmsMPAs);

    }
}
