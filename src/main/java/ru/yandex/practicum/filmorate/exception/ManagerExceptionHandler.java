package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.model.Code;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ManagerExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Code>> handleValidationError(ValidationException exception) {
        log.error("Возникла ошибка валидации данных: {}", exception.toString());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Code>> handleUpdateException(EntityNotFoundException exception) {
        log.error("Возникла ошибка при обновлении объекта: {}", exception.toString());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ObjectFoundException.class)
    public ResponseEntity<Map<String,Code>> handleObjectFoundException(ObjectFoundException exception) {
        log.error("При попытке обратиться к объекту возникла ошибка: {}", exception.toString());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

}
