package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.properties.Genre;
import ru.yandex.practicum.filmorate.utility.CommonHelper;


import java.util.*;

import static ru.yandex.practicum.filmorate.utility.CommonHelper.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class JbdcGenreStorage implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final CommonHelper commonHelper;


    @Override
    public void createGenre(Film film) {
        if (film.getGenres() == null) {
            MapSqlParameterSource genreParams = new MapSqlParameterSource();
            genreParams.addValue("filmId", film.getId());
            genreParams.addValue("genreId", null);
            jdbc.update("INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (:filmId, :genreId)", genreParams);
        } else {
            String sql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (:filmId, :genreId)";
            List<MapSqlParameterSource> batchParams = new ArrayList<>();
            for (Genre genre : film.getGenres()) {
                commonHelper.checkGenreId(genre);
                MapSqlParameterSource genreParams = new MapSqlParameterSource();
                genreParams.addValue("filmId", film.getId());
                genreParams.addValue("genreId", genre.getId());
                batchParams.add(genreParams);
            }
            jdbc.batchUpdate(sql, batchParams.toArray(new MapSqlParameterSource[0]));
        }
    }


    @Override
    public void deleteGenre(Film film) {
        String sql = "DELETE FROM FILM_GENRE WHERE FILM_ID = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", film.getId());
        jdbc.update(sql, params);
    }

    @Override
    public HashSet<Genre> getGenre(Long id) {
        String sql = "SELECT g.GENRE_ID, g.GENRE_NAME FROM GENRE g " +
                "JOIN FILM_GENRE fg ON g.GENRE_ID = fg.GENRE_ID " +
                "WHERE fg.FILM_ID = :filmId " +
                "ORDER BY g.GENRE_ID ASC";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", id);
        List<Genre> genres = jdbc.query(sql, params, (rs, rowNum) -> {
            return new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"));
        });
        return new LinkedHashSet<>(genres);
       // Да,для тестов в Postman важен порядок вывода жанров,в частности тест Film create several genres выдаёт ошибку
        // Test film 'genres' field | AssertionError: "genres" field must be %{requestGenres}: expected [ 5, 3 ] to deeply equal [ 3, 5 ]

    }

    @Override
    public Genre getGenreById(Long id) {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE WHERE GENRE_ID = :genreId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", id);
        return jdbc.queryForObject(sql, params, (rs, rowNum) ->
                new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME"))
        );
    }

    @Override
    public Set<Genre> getAllGenres() {
        String sql = "SELECT GENRE_ID, GENRE_NAME FROM GENRE";
        List<Genre> genres = jdbc.query(sql, (rs, rowNum) ->
                new Genre(rs.getLong("GENRE_ID"), rs.getString("GENRE_NAME")));
        return new LinkedHashSet<>(genres);
    }
}
