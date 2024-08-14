package ru.yandex.practicum.filmorate.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.exception.model.Code;

@EqualsAndHashCode(callSuper = false)
@Data
public class ObjectFoundException extends RuntimeException {
    private final Code code;

    public ObjectFoundException(String message) {
        super(message);
        this.code = Code.OBJECT_ERROR;
    }
}
