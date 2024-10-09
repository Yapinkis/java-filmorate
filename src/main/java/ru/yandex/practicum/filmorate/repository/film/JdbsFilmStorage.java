package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.MPA;
import ru.yandex.practicum.filmorate.utility.CommonHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class JdbsFilmStorage implements FilmRepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO FILM (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "VALUES (:name, :description, :releaseDate, :duration, :ratingId)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("ratingId",film.getMpa().getId());
        jdbc.update(sqlQuery, params, keyHolder, new String[] {"Film_ID"});
        Long generatedId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(generatedId);
        CommonHelper.setFilmsCount(getMaxFilmCount());
        log.info("Создан фильм ={}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        String updateQuery = "UPDATE FILM SET FILM_NAME = :name, DESCRIPTION = :description, " +
                "RELEASE_DATE = :releaseDate, DURATION = :duration, RATING_ID = :rating " +
                "WHERE FILM_ID = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", film.getId());
        params.addValue("name", film.getName());
        params.addValue("description", film.getDescription());
        params.addValue("releaseDate", film.getReleaseDate());
        params.addValue("duration", film.getDuration());
        params.addValue("rating", film.getMpa().getId());
        int rowsAffected = jdbc.update(updateQuery, params);
        CommonHelper.checkRow(rowsAffected);
        log.info("Обновлен фильм ={}", film.getId());
        return film;
    }

    @Override
    public List<Long> getAll() {
        String sqlFilms = "SELECT FILM_ID FROM FILM ORDER BY FILM_ID";
        MapSqlParameterSource params = new MapSqlParameterSource();
        return jdbc.queryForList(sqlFilms,params,Long.class);
    }

    @Override
    public Film get(Long id) {
        String sqlQuery = "SELECT f.FILM_ID, f.FILM_NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, " +
                "f.RATING_ID " +
                "FROM FILM f " +
                "WHERE f.FILM_ID = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbc.queryForObject(sqlQuery, params, (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("FILM_ID"));
            film.setName(rs.getString("FILM_NAME"));
            film.setDescription(rs.getString("DESCRIPTION"));
            film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
            film.setDuration(rs.getLong("DURATION"));
            MPA mpa = new MPA();
            mpa.setId(rs.getLong("RATING_ID"));
            film.setMpa(mpa);
            return film;
        });
    }

    public Long getMaxFilmCount() {
        String sqlQuery = "SELECT MAX(FILM_ID) FROM FILM";
        Long number = jdbc.queryForObject(sqlQuery, new HashMap<>(), Long.class);
        return number == null ? 0 : number;
    }
    //аналогично User

}
