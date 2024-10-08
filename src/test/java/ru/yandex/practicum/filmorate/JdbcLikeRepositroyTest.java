package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.repository.user.JbdcLikeStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Import(JbdcLikeStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcLikeRepository")
public class JdbcLikeRepositroyTest {
    public static final long USER_ID_ONE = 1L;
    public static final long USER_ID_TWO = 2L;
    public static final long USER_ID_THREE = 3L;
    public static final long USER_ID_FOUR = 4L;
    public static final long FILM_ID_ONE = 1L;
    public static final long FILM_ID_TWO = 2L;
    public static final long FILM_ID_THREE = 3L;

    private final JbdcLikeStorage jbdcLikeStorage;

    static User createUser(Long id, String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }

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

    static List<User> getTestUsers() {
        return List.of(
                createUser(USER_ID_ONE, "something@ya.ru", "login_One", "Test_Name_One",
                        LocalDate.of(1985, 11, 10)),
                createUser(USER_ID_TWO, "other@ya.ru", "login_Two", "Test_Name_Two",
                        LocalDate.of(1990, 8, 12)),
                createUser(USER_ID_THREE,"maybe@ya.ru","login_Three", "Test_Name_Three",
                        LocalDate.of(1995, 4, 6)),
                createUser(USER_ID_FOUR,"new@ya.ru","login_Four", "Test_Name_Four",
                        LocalDate.of(1993, 2, 3))
        );
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
    static User getUser(Long num) {
        return getTestUsers().stream().filter(findUser -> findUser.getId() == num).
                findFirst().orElseThrow(() -> new EntityNotFoundException("User не найден"));
    }
    static Film getFilm(Long num) {
        return getTestFilms().stream().filter(film -> film.getId() == num).findFirst().orElseThrow(()
                -> new EntityNotFoundException("Film не найден"));
    }

    @Test
    @DisplayName("Добавляет значение параметра likes модели Film")
    public void addLikeMethod_add_value_to_likeParameter_from_Film() {
        Film film = getFilm(FILM_ID_ONE);
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        User user3 = getUser(USER_ID_THREE);

        jbdcLikeStorage.addLike(film,user1);
        jbdcLikeStorage.addLike(film,user2);
        jbdcLikeStorage.addLike(film,user3);

        film.setLikes(jbdcLikeStorage.getFilmLikes(film));

        assertThat(film.getLikes().size()).isEqualTo(USER_ID_THREE);
    }

    @Test
    @DisplayName("Возвращает List параметра likes модели Film в порядке добавления лайков пользователями User")
    public void getFilmLikesMethod_return_List_likeParameter_from_Film_in_order_of_addition() {
        Film film = getFilm(FILM_ID_ONE);
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        User user3 = getUser(USER_ID_THREE);

        LinkedHashSet<Long> linkedLikes = new LinkedHashSet<>();
        linkedLikes.add(USER_ID_TWO);
        linkedLikes.add(USER_ID_ONE);
        linkedLikes.add(USER_ID_THREE);

        jbdcLikeStorage.addLike(film,user2);
        jbdcLikeStorage.addLike(film,user1);
        jbdcLikeStorage.addLike(film,user3);

        film.setLikes(jbdcLikeStorage.getFilmLikes(film));
        assertThat(film.getLikes()).isEqualTo(linkedLikes);
    }

    @Test
    @DisplayName("Возвращает List модели Film с наибольшим количеством параметра likes в порядке от большего к меньшему")
    public void getMostPopularFilmsMethod_return_List_Film_with_highest_value_of_likes() {
        Film film1 = getFilm(FILM_ID_ONE);
        Film film2 = getFilm(FILM_ID_TWO);
        Film film3 = getFilm(FILM_ID_THREE);
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        User user3 = getUser(USER_ID_THREE);
        User user4 = getUser(USER_ID_FOUR);

        //Добавляем likes для film1
        jbdcLikeStorage.addLike(film1,user1);
        //Добавляем likes для film2
        jbdcLikeStorage.addLike(film2,user1);
        jbdcLikeStorage.addLike(film2,user2);
        //Добавляем likes для film3
        jbdcLikeStorage.addLike(film3,user1);
        jbdcLikeStorage.addLike(film3,user2);
        jbdcLikeStorage.addLike(film3,user3);
        jbdcLikeStorage.addLike(film3,user4);

        List<Film> mostPopular = jbdcLikeStorage.getMostPopularFilms(2);
        assertThat(film3.getId()).isEqualTo(mostPopular.get(0).getId());
        assertThat(film2.getId()).isEqualTo(mostPopular.get(1).getId());
        //Просто сравниваю Id-шники,т.к. для заполнения объектов из БД понадобятся методы из других репозиториев
    }

}
