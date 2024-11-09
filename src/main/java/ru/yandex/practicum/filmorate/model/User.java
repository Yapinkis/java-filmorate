package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.properties.FriendshipStatus;


import java.time.LocalDate;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    @NotNull @Email
    private String email;
    @NotNull @Pattern(regexp = "\\S+")
    private String login;
    @NotNull
    private String name;
    @PastOrPresent @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private Map<Long, FriendshipStatus> friendships;
}
