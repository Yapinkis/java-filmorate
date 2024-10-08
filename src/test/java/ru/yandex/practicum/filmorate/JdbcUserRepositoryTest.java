package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserStorage;

import java.time.LocalDate;
import java.util.List;


@JdbcTest
@Import(JdbcUserStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcUserRepository")
public class JdbcUserRepositoryTest {
    public static final long USER_ID_ONE = 1L;
    public static final long USER_ID_TWO = 2L;
    public static final long USER_ID_THREE = 3L;
    public static final long USER_ID_FOUR = 4L;
    public static final long USER_ID_FOR_TEST = 5L;
    //Наверное использовать @Qualifier нет никакого смысли т.к. у меня по одной реализации каждого интерфейса
    //и можно явно указать
    private final JdbcUserStorage jdbcUserStorage;

    static User createUser(Long id,String email,String login,String name,LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
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

    @Test
    @DirtiesContext
    //По какой-то причине моя БД не обновляется после каждого теста и если убрать аннотацию @DirtiesContext
    //то будет создан User с Id = 4. Возможно это как-то связано с тем, что у меня прописан автоинкремент в USERS для
    //идишников, но почему БД не обновляется после каждого теста?
    //Мне даже конфигурация spring.datasource.url=jdbc:h2:mem:testdb не помогла
    @DisplayName("Добавляет нового User в тестовое окружение")
    public void addUserMethod_should_return_new_User() {
        User user = createUser(USER_ID_FOR_TEST,"new@ya.ru","login_Four",
                "Test_Name_Four",LocalDate.of(1993, 2, 3));
        jdbcUserStorage.addUser(user);
        User newUser = jdbcUserStorage.get(user.getId());
        assertThat(user).usingRecursiveComparison().isEqualTo(newUser);
    }

    @Test
    @DisplayName("Обновляет User по заданным параметрам")
    public void updateMethod_should_updated_User() {
        User user = createUser(USER_ID_FOR_TEST,"notFromDB@ya.ru","login_notFromDB",
                "Test_Name_NotFromDB",LocalDate.of(1988, 12, 13));
        jdbcUserStorage.addUser(user);
        user.setName("new_name");
        user.setEmail("new_email@ya.ru");
        jdbcUserStorage.update(user);
        User updatedUser = jdbcUserStorage.get(USER_ID_FOR_TEST);
        assertThat(updatedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    @DisplayName("Возвращает список User с значением их Id")
    public void getUsersMethod_should_return_Users_List_with_test_Id() {
        List<Long> Users = jdbcUserStorage.getAll();
        List<Long> fromTestMethod = getTestUsers().stream().map(User::getId).toList();
        assertThat(Users).usingRecursiveComparison().isEqualTo(fromTestMethod);
    }

    @Test
    @DisplayName("Возвращает экземпляр User по указанному Id")
    public void getMethod_should_return_User_with_test_Id() {
        User user = jdbcUserStorage.get(USER_ID_THREE);
        User fromTestMethod = getTestUsers().stream().filter(findUser -> findUser.getId() == USER_ID_THREE).
                findFirst().orElseThrow(() -> new EntityNotFoundException("User не найден"));
        assertThat(user).usingRecursiveComparison().isEqualTo(fromTestMethod);
    }
}

