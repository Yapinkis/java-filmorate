package ru.yandex.practicum.filmorate.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import ru.yandex.practicum.filmorate.exception.model.Code;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ManagerExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public ResponseEntity<Map<String, Code>> handleValidationError(ValidationException exception) {
        log.error("Возникла ошибка валидации данных: {}", exception.getMessage());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Code>> handleUpdateException(EntityNotFoundException exception) {
        log.error("Возникла ошибка при обновлении объекта: {}", exception.getMessage());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherException(Exception exception) {
        if (exception instanceof HandlerMethodValidationException) {
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        }
        log.error("При попытке обратиться к объекту возникла ошибка: {}", exception.getMessage());
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
