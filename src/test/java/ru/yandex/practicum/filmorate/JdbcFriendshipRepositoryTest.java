package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.properties.FriendshipStatus;
import ru.yandex.practicum.filmorate.repository.user.JdbcFriendshipStorage;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@JdbcTest
@Import(JdbcFriendshipStorage.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DisplayName("JdbcFriendshipRepository")
public class JdbcFriendshipRepositoryTest {
    public static final long USER_ID_ONE = 1L;
    public static final long USER_ID_TWO = 2L;
    public static final long USER_ID_THREE = 3L;
    public static final long USER_ID_FOUR = 4L;

    private final JdbcFriendshipStorage jdbcFriendshipStorage;

    static User createUser(Long id, String email, String login, String name, LocalDate birthday) {
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

    static User getUser(Long num) {
        return getTestUsers().stream().filter(findUser -> findUser.getId() == num).findFirst().orElseThrow(() -> new EntityNotFoundException("User не найден"));
    }

    @Test
    @DisplayName("Возвращает статус дружбы в соотношении: User1 имеет статус држубы с пользователем User2 - CONFIRMED при добавлении в друзья методом addFriend")
    public void addFriendMethod_should_return_friendship_status_for_User1_to_User2() {
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        jdbcFriendshipStorage.addFriend(user1,user2);// 1
        user1.setFriendships(jdbcFriendshipStorage.setFriendship(USER_ID_ONE));// 2
        //Подобный код фактически проверяет действие двух моих методов.
        // 1 - добавляет в таблицу FRIENDSHIP
        // 2 - устанавливает дружбу
        // Можно ли так сделать?Или тестирование предполагает проверку каждого теста изолировано от другого?
        // Если изолировано, то мне получается нужно описать отдельный тестовый метод для обращения к БД,
        // допустим что бы узнать общее количество записей в таблице FRIENDSHIP?
        assertThat(user1.getFriendships()).containsEntry(USER_ID_TWO, FriendshipStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Не будет возвращать статус држубы - CONFIRMED для пользователя User2 при добавлении User1 в друзья методом addFriend")
    public void addFriendMethod_will_not_return_friendship_status_User2_to_User1() {
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        jdbcFriendshipStorage.addFriend(user1,user2);
        user1.setFriendships(jdbcFriendshipStorage.setFriendship(USER_ID_ONE));
        assertThat(user2.getFriendships()).isNull();
    }

    @Test
    @DisplayName("Удаляет из друзей User1 пользователя UserN методом removeFriend")
    public void removeFriendMethod_should_remove_friendship_for_User() {
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        User user3 = getUser(USER_ID_THREE);
        //log.info("Добавление user1 друзей {} и {}",user2.getName(), user3.getName());
        //Добавлять логи в тесты наверное излишне
        jdbcFriendshipStorage.addFriend(user1,user2);
        jdbcFriendshipStorage.addFriend(user1,user3);
        user1.setFriendships(jdbcFriendshipStorage.setFriendship(USER_ID_ONE));
        Map<Long,FriendshipStatus> beforeRemove = user1.getFriendships();

        jdbcFriendshipStorage.removeFriend(user1,user2);
        user1.setFriendships(jdbcFriendshipStorage.setFriendship(USER_ID_ONE));
        Map<Long,FriendshipStatus> afterRemove = user1.getFriendships();

        assertThat(beforeRemove).isNotEqualTo(afterRemove);
    }

    @Test
    @DisplayName("Возвращает список общих друзей")
    public void getCommonFriendsMethod_should_return_common_friends_List() {
        User user1 = getUser(USER_ID_ONE);
        User user2 = getUser(USER_ID_TWO);
        User commonFriend1 = getUser(USER_ID_THREE);
        User commonFriend2 = getUser(USER_ID_FOUR);

        List<Long> commonFriends = new ArrayList<>();
        commonFriends.add(commonFriend1.getId());
        commonFriends.add(commonFriend2.getId());

        jdbcFriendshipStorage.addFriend(user1,user2);
        jdbcFriendshipStorage.addFriend(user1,commonFriend1);
        jdbcFriendshipStorage.addFriend(user1,commonFriend2);
        jdbcFriendshipStorage.addFriend(user2,user1);
        jdbcFriendshipStorage.addFriend(user2,commonFriend1);
        jdbcFriendshipStorage.addFriend(user2,commonFriend2);

        List<Long> commonFriendsFromDB = jdbcFriendshipStorage.getCommonFriends(USER_ID_ONE,USER_ID_TWO);
        assertThat(commonFriends).usingRecursiveComparison().isEqualTo(commonFriendsFromDB);
    }

}
