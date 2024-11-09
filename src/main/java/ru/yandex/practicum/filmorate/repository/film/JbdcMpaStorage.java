package ru.yandex.practicum.filmorate.repository.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.properties.MPA;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JbdcMpaStorage implements MPARepository {
    private final NamedParameterJdbcOperations jdbc;

    @Override
    public MPA getMpaRating(Long id) {
        String sqlQueryMpa = "SELECT * FROM MPA WHERE RATING_ID = :ratingId";
        MapSqlParameterSource paramsMpa = new MapSqlParameterSource();
        paramsMpa.addValue("ratingId", id);
        return jdbc.queryForObject(sqlQueryMpa, paramsMpa, (rs, rowNum) ->
                new MPA(rs.getLong("RATING_ID"), rs.getString("RATING_NAME")));
    }

    @Override
    public List<MPA> getAllMpaRatings() {
        String sqlQuery = "SELECT RATING_ID, RATING_NAME FROM MPA";
        return jdbc.query(sqlQuery, (rs, rowNum) -> {
            return new MPA(rs.getLong("RATING_ID"), rs.getString("RATING_NAME"));
        });
    }
}
