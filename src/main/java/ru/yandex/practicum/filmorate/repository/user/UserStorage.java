//package ru.yandex.practicum.filmorate.repository.user;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.model.User;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Component
//@Slf4j
//public class UserStorage implements UserRepository {
//    private Long userId = 0L;
//    Map<Long,User> users = new HashMap<>();
//    Map<Long,Set<Long>> userFriends = new HashMap<>();
//
//    @Override
//    public User addUser(User user) {
//        users.put(user.getId(),user);
//        return user;
//    }
//
//    @Override
//    public User get(Long id) {
//        return users.get(id);
//    }
//    @Override
//    public void addFriend(User user, User userFriend) {
//        Set<Long> masterUserFriends = userFriends.computeIfAbsent(user.getId(),key -> new HashSet<>());
//        masterUserFriends.add(userFriend.getId());
//        Set<Long> userFriendFriends = userFriends.computeIfAbsent(userFriend.getId(), key -> new HashSet<>());
//        userFriendFriends.add(user.getId());
//    }
//    @Override
//    public void deleteFriend(User user, User userFriend) {
//        userFriends.computeIfPresent(user.getId(), (idOfUser, friends) -> {friends.remove(userFriend.getId());
//            return friends.isEmpty() ? null : friends;});
//        userFriends.computeIfPresent(userFriend.getId(), (idOfUserFriend, friends) -> {friends.remove(user.getId());
//            return friends.isEmpty() ? null : friends;});
//    }
//
//    @Override
//    public List<User> getMutualFriends(User user, User userFriend) {
//        Set<Long> userList = userFriends.get(user.getId());
//        Set<Long> userFriendList = userFriends.get(userFriend.getId());
//        if (userList == null || userFriendList == null) {
//            return Collections.emptyList();
//        }
//        userList.retainAll(userFriendList);
//        return userList.stream().map(users::get).collect(Collectors.toList());
//    }
//
//    @Override
//    public List<User> getFriends(User user) {
//        Set<Long> friendsList = userFriends.get(user.getId());
//        if (friendsList == null || friendsList.isEmpty()) {
//            return Collections.emptyList();
//        }
//        return friendsList.stream().map(users::get).collect(Collectors.toList());
//    }
//    public List<User> getUsers() {
//        return new ArrayList<>(users.values());
//    }
//
//    @Override
//    public User update(User element) {
//        return null;
//    }
//
//    @Override
//    public List<User> getAll() {
//        return List.of();
//    }
//}
