package br.com.uff.askme.error.handler;

import br.com.uff.askme.error.StandardError;
import br.com.uff.askme.error.ValidationError;
import br.com.uff.askme.error.exception.BadRequestException;
import br.com.uff.askme.error.exception.NotFoundException;
import br.com.uff.askme.error.exception.UnauthorizedException;
import br.com.uff.askme.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@AllArgsConstructor
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<StandardError> handle(MethodArgumentNotValidException exception) {
        List<StandardError> standardErrors = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {
            standardErrors.add(ValidationError.builder()
                            .title("Bad Request Exception. Invalid fields.")
                            .status(HttpStatus.BAD_REQUEST.value())
                            .errorMessage(fieldError.getDefaultMessage())
                            .developerMessage(exception.getClass().getName())
                            .dateTime(getDateTime())
                            .field(fieldError.getField())
                            .build());

            log.error("Erro de validação no campo " + fieldError.getField() + " para se criar ou atualizar recurso");
        });

        return standardErrors;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> handleNotFound(NotFoundException notFoundException) {
        return new ResponseEntity<>(StandardError.builder()
                .title("Object Not Found Exception. Check documentation.")
                .status(HttpStatus.NOT_FOUND.value())
                .errorMessage(notFoundException.getMessage())
                .developerMessage(notFoundException.getClass().getName())
                .dateTime(getDateTime())
                .build(), HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<StandardError> handleBadRequest(BadRequestException badRequestException) {
        return new ResponseEntity<>(StandardError.builder()
                .title("Bad Request Exception.")
                .status(HttpStatus.BAD_REQUEST.value())
                .errorMessage(badRequestException.getMessage())
                .developerMessage(badRequestException.getClass().getName())
                .dateTime(getDateTime())
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardError> handleForbidden(UnauthorizedException unauthorizedException) {
        return new ResponseEntity<>(StandardError.builder()
                .title("Unauthorized Exception.")
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorMessage(unauthorizedException.getMessage())
                .developerMessage(unauthorizedException.getClass().getName())
                .dateTime(getDateTime())
                .build(), HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StandardError> handleNotSupported(HttpRequestMethodNotSupportedException notSupportedException) {
        return new ResponseEntity<>(StandardError.builder()
                .title("Method Not Supported Exception")
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .errorMessage(notSupportedException.getMessage())
                .developerMessage(notSupportedException.getClass().getName())
                .dateTime(getDateTime())
                .build(), HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    private String getDateTime() {
        return DateUtil.formatLocalDateTime(LocalDateTime.now());
    }

}
