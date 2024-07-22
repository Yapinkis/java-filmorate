package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Наверное нам здесь не нужен конструктор без параметров.
// А когда кстати нам могут понадобиться конструкторы без параметров?
public class Film {

    private Integer id;
    @NonNull //Нужна ли здесь эта аннотация, если я использую выброс пользовательского исключения?И если да, то может
    //её объявить на уровне класса, что бы все поля не могли быть null
    private String name;
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private Long duration;
    //Можно же использовать Long или тип данных обязательно должен быть Duration?

}
