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

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleException(final Exception e) {
//        log.warn("Ошибка", e);
//        return new ErrorResponse(e.getMessage(),e);
//    } Мы подобный обработчик писали в лекции,я так понимаю его основной смысл в том что отправить стэк-трейс
//    и позже выдат исключение которое не возвращает код ошибки. У меня все исключения возвращают код ошибки, поэтому
//    наверное в ErrorResponse нет смысла. Так же как и выводить стэк-трейс в моих исключениях или нет?
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Code>> handleValidationError(ValidationException exception) {
        log.warn("Возникла ошибка валидации данных: {}", exception.toString());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Code>> handleUpdateException(EntityNotFoundException exception) {
        log.warn("Возникла ошибка при обновлении объекта: {}", exception.toString());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Map<String,Code>> handleObjectFoundException(ServerException exception) {
        log.warn("При попытке обратиться к объекту возникла ошибка: {}", exception.toString());
        Map<String,Code> response = new HashMap<>();
        response.put(exception.getMessage(),exception.getCode());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
