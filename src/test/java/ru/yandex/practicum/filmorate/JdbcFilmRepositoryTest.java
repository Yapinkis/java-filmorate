package ru.yandex.practicum.filmorate;

import static org.assertj.core.api.Assertions.assertThat;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.repository.film.JdbsFilmStorage;

import java.time.LocalDate;
import java.util.List;

@JdbcTest
@Import(JdbsFilmStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFilmRepository")
public class JdbcFilmRepositoryTest {
    public static final long FILM_ID_ONE = 1L;
    public static final long FILM_ID_TWO = 2L;
    public static final long FILM_ID_THREE = 3L;
    public static final long FILM_ID_FOR_TEST = 4L;

    private final JdbsFilmStorage jdbsFilmStorage;

    static Film createFilm(Long Id,String name,String description,Long duration,LocalDate releaseDate,MPA mpa) {
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

    static Film getFilm(Long num) {
        return getTestFilms().stream().filter(film -> film.getId() == num).findFirst().orElseThrow(()
                -> new EntityNotFoundException("Film не найден"));
    }

    @Test
    @DisplayName("Добавляет новый Film в тестовое окружение")
    public void addFilmMethod_should_return_new_Film() {
        Film film = createFilm(FILM_ID_FOR_TEST,"Four_Film","Four_Description",140L,
                LocalDate.of(2019,11,10),new MPA(FILM_ID_THREE,null));
        // Здесь в MPA присутствует null т.к. получение рейтинга MPA у нас осуществляется при помощи другого репозитория
        // а необходимость его присутствия обусловлена архитектурой таблицы FILM которая содержит внешний ключ
        // ссылающийся на таблицу MPA
        jdbsFilmStorage.addFilm(film);
        Film created = jdbsFilmStorage.get(film.getId());
        assertThat(film).usingRecursiveComparison().isEqualTo(created);
    }

    @Test
    @DisplayName("Обновляет Film по заданным параметрам")
    public void updateMethod_should_return_updated_Film() {
        Film film = createFilm(FILM_ID_FOR_TEST,"Four_Film","Four_Description",140L,
                LocalDate.of(2019,11,10),new MPA(FILM_ID_THREE,null));
        jdbsFilmStorage.addFilm(film);
        film.setName("New_Name");
        film.setDescription("New_Description");
        jdbsFilmStorage.update(film);
        Film updatedFilm = jdbsFilmStorage.get(FILM_ID_FOR_TEST);
        assertThat(film).usingRecursiveComparison().isEqualTo(updatedFilm);
    }

    @Test
    @DisplayName("Возвращает список всех Film с значением их Id")
    public void getAllMethod_should_return_List_with_test_Id() {
        List<Long> films = jdbsFilmStorage.getAll();
        List<Long> filmsFromTestMethod = getTestFilms().stream().map(Film::getId).toList();
        assertThat(films).usingRecursiveComparison().isEqualTo(filmsFromTestMethod);
    }

}
