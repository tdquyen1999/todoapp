package todo.app.handler;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import todo.app.exception.NotFoundException;

@RestControllerAdvice
public class TaskExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundAnyTask(Exception exception) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}
