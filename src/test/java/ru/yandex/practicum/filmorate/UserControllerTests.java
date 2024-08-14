package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmorateManager;

import java.time.LocalDate;

@SpringBootTest
public class UserControllerTests {
    private FilmorateManager filmorateManager;

    @BeforeEach
    void setUp() {
        filmorateManager = new FilmorateManager();
    }



    @DisplayName("Электронная почта не может быть пустой и должна содержать символ @")
    @Test
    void validateUser_throwValidationException_emailNullOrDoesNotContainSymbol() {
        User user = new User();
        user.setName("Name");
        user.setEmail("@Email");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1980,12,28));
        user.setId(filmorateManager.nextUserId());
        // Или лучше создавать тестовый объект так? Смущает просто дублирование кода
        Assertions.assertThrows(ValidationException.class, () -> {
            user.setEmail("Other");
            filmorateManager.validateUser(user);
        });
    }

    @DisplayName("Логин не может быть пустым и содержать пробелы")
    @Test
    void validateUser_throwValidationException_loginNull() {
        User user = new User();
        user.setName("Name");
        user.setEmail("@Email");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1980,12,28));
        user.setId(filmorateManager.nextUserId());
        Assertions.assertThrows(ValidationException.class, () -> {
            user.setLogin("Other user");
            filmorateManager.validateUser(user);
        });
    }

    @DisplayName("Дата рождения не может быть в будущем")
    @Test
    void validateUser_throwValidationException_userBirthdayInFuture() {
        User user = new User();
        user.setName("Name");
        user.setEmail("@Email");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1980,12,28));
        user.setId(filmorateManager.nextUserId());
        Assertions.assertThrows(ValidationException.class, () -> {
            user.setBirthday(LocalDate.now().plusDays(1));
            filmorateManager.validateUser(user);
        });
    }

    @DisplayName("Используется логин вместо имени")
    @Test
    void validateUser_throwValidationException_userLoginInsteadOfName() {
        User user = new User();
        user.setName("Name");
        user.setEmail("@Email");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(1980,12,28));
        user.setId(filmorateManager.nextUserId());
        user.setName(null);
        filmorateManager.validateUser(user);
        Assertions.assertEquals(user.getLogin(),user.getName());
    }
}