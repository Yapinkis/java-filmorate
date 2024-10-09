package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.LinkedHashSet;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JbdcLikeStorage implements LikeRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public void addLike(Film film, User userLike) {
        String sqlQuery = "INSERT INTO FILMS_LIKE (FILM_ID, USER_ID) VALUES (:filmId, :userId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", film.getId());
        params.addValue("userId", userLike.getId());
        jdbc.update(sqlQuery, params);
        log.info("Пользователь с ID {} добавил лайк фильму с ID {}.", userLike.getId(), film.getId());
    }

    @Override
    public void removeLike(Film film, User userLike) {
        String sqlQuery = "DELETE FROM FILMS_LIKE WHERE USER_ID = :userId AND FILM_ID = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userLike.getId());
        params.addValue("filmId", film.getId());
        jdbc.update(sqlQuery, params);
    }

    @Override
    public LinkedHashSet<Long> getFilmLikes(Film film) {
        String sqlQuery = "SELECT USER_ID FROM FILMS_LIKE WHERE FILM_ID = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", film.getId());
        List<Long> userLikes = jdbc.query(sqlQuery, params, (rs, rowNum) -> rs.getLong("USER_ID"));
        log.info("Для фильма с ID {} добавлены лайки", film.getId());
        return new LinkedHashSet<>(userLikes);
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        String sqlQuery = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID, " +
                "COUNT(fl.USER_ID) AS like_count " +
                "FROM FILM f " +
                "LEFT JOIN FILMS_LIKE fl ON f.FILM_ID = fl.FILM_ID " +
                "GROUP BY f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.RATING_ID " +
                "ORDER BY like_count DESC " +
                "LIMIT :count";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("count", count);
        return jdbc.query(sqlQuery, params, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("FILM_ID"));
            film.setName(rs.getString("FILM_NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(rs.getLong("DURATION"));
            return film;
        });
    }
}
