package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTests {

	private FilmorateManager filmorateManager;

	@BeforeEach
	void setUp() {
		filmorateManager = new FilmorateManager();
	}

	private Film createFilm() {
		Film film = new Film();
		film.setName("Name");
		film.setDescription("Description");
		film.setReleaseDate(LocalDate.now());
		film.setDuration(100L);
		film.setId(filmorateManager.nextFilmId());
		return film;
		//Я немного переписал код,но это не сильно меняет суть т.к. теперь у меня метод на уровне класса,
		// и там инициализируется перемнная.
		// Я исхожу из 5 спринта, где мы создавали переменную на уровне класса и инициализировали
		// её под аннотацией @BeforeEach.
		// Или мне всё-таки стоит ещё поучить матчасть?)
	}

	@Test
	@DisplayName("Названеи фильма не может быть пустым")
	void validateFilm_throwValidationException_fieldNameNull() {
		Film film = createFilm();
		// Просто если следовать структуре given-that-then как я понимаю мне нужно в каждом тесте создавать свой
		// отдельный экземпляр со своими независимыми данными, но это приводит к дублированию кода?
		Assertions.assertThrows(ValidationException.class, () -> {
			film.setName("");
			filmorateManager.validateFilm(film);
		});
	}

	@Test
	@DisplayName("Добавление фильма с описанием более 200 символов")
	void validateFilm_throwValidationException_descriptionLongerThan200() {
		Film film = createFilm();
		Assertions.assertThrows(ValidationException.class, () -> {
			for (int i = 0; i < 20; i++) {
				film.setDescription(film.getDescription() + "Something++");
			}
			filmorateManager.validateFilm(film);
		});

	}

	@Test
	@DisplayName("Дата релиза фильма до 28 декабря 1895 года")
	void validateFilm_throwValidationException_releaseDateBefore1895() {
		Film film = createFilm();
		Assertions.assertThrows(ValidationException.class, () -> {
			film.setReleaseDate(LocalDate.of(1895,12,27));
			filmorateManager.validateFilm(film);
		});
	}

	@Test
	@DisplayName("Продолжительность фильма не может быть отрицательным числом")
	void validateFilm_throwValidationException_filmDurationANegativeNumber() {
		Film film = createFilm();
		Assertions.assertThrows(ValidationException.class, () -> {
			film.setDuration(-20L);
			filmorateManager.validateFilm(film);
		});
	}

	@Test
	@DisplayName("Ошибка при обновлении фильма при отсутствии Id")
	void validateFilm_throwValidationException_filmIdNull() {
		Film film = createFilm();
		Assertions.assertThrows(EntityNotFoundException.class, () -> {
			film.setId(null);
			filmorateManager.validateUpdateFilm(film);
		});
	}

}
