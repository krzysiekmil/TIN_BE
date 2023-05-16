package pjwstk.s20124.tin.web.advice;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.StreamSupport;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseError handleConstraintViolation(ConstraintViolationException ex) {
        log.debug("Constraint violation exception encountered: {}", ex.getConstraintViolations());
        return ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .status(HttpStatus.BAD_REQUEST.name())
            .errors(buildValidationErrors(ex.getConstraintViolations()))
            .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError handleArgumentNotValid(MethodArgumentNotValidException ex) {
        log.debug("Validation exception encountered: {}", ex.getMessage(), ex);
        return ResponseError.builder()
            .code(HttpStatus.BAD_REQUEST.value())
            .status(HttpStatus.BAD_REQUEST.name())
            .errors(buildValidationErrors(ex.getFieldErrors()))
            .build();
    }

    private List<ValidationError> buildValidationErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .map(fieldError ->
                ValidationError.builder()
                    .objectName(fieldError.getObjectName().replaceFirst("DTO$", ""))
                    .field(fieldError.getField())
                    .error(StringUtils.isNotBlank(fieldError.getDefaultMessage()) ? fieldError.getDefaultMessage() : fieldError.getCode())
                    .build())
            .toList();
    }

    private List<ValidationError> buildValidationErrors(Set<ConstraintViolation<?>> violations) {
        return violations.stream().
            map(violation ->
                ValidationError.builder()
                    .field(
                        Objects.requireNonNull(StreamSupport.stream(violation.getPropertyPath().spliterator(), false)
                                .reduce((first, second) -> second)
                                .orElse(null))
                            .toString()
                    )
                    .error(violation.getMessage())
                    .build())
            .toList();
    }
}
