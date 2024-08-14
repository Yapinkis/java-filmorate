package ru.yandex.practicum.filmorate.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.exception.model.Code;

@EqualsAndHashCode(callSuper = false)
@Data
public class ServerException extends RuntimeException {
    private final Code code;

    public ServerException(String message) {
        super(message);
        this.code = Code.SERVER_ERROR;
    }
}
