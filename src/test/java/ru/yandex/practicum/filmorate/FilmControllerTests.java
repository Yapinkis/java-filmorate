package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.UpdateException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmorateManager;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTests {

	private FilmorateManager filmorateManager;
	private Film film;

	@BeforeEach
	void setUp() {
		filmorateManager = new FilmorateManager();
		film = new Film();
		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.now());
		film.setDuration(100L);
		film.setId(filmorateManager.nextFilmId());
	}

	@Test
	@DisplayName("Названеи фильма не может быть пустым")
	void FilmNameCannotBeNull() {
		Assertions.assertThrows(ValidationException.class, ()-> {
			film.setName("");
			filmorateManager.validateFilm(film);
		});
	}

	@Test
	@DisplayName("Добавление фильма с описанием более 200 символов")
	void FilmDescriptionMoreThen200Characters() {
		Assertions.assertThrows(ValidationException.class, () -> {
			for (int i = 0; i < 20; i++) {
				film.setDescription(film.getDescription() + "Something++");
			}
			filmorateManager.validateFilm(film);
		});

	}

	@Test
	@DisplayName("Дата релиза фильма до 28 декабря 1895 года")
	void FilmReleaseDateBeforeCheckDate() {
		Assertions.assertThrows(ValidationException.class, ()-> {
			film.setReleaseDate(LocalDate.of(1895,12,27));
			filmorateManager.validateFilm(film);
		});
	}

	@Test
	@DisplayName("Продолжительность фильма не может быть отрицательным числом")
	void FilmDurationCannotBeANegativeNumber() {
		Assertions.assertThrows(ValidationException.class, ()-> {
			film.setDuration(-20L);
			filmorateManager.validateFilm(film);
		});
	}

	@Test
	@DisplayName("Ошибка при обновлении фильма при отсутствии Id")
	void FilmUpdateErrorWithoutId() {
		Assertions.assertThrows(UpdateException.class, ()-> {
			film.setId(null);
			filmorateManager.validateUpdateFilm(film);
		});
	}

}
