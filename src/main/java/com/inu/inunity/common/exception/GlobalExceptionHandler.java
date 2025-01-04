package com.inu.inunity.common.exception;

import com.inu.inunity.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = UserRegisteredException.class)
    public ResponseEntity<CommonResponse> handleAlreadyRegisteredException(UserRegisteredException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handleAlreadyRegisteredException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }

    @ExceptionHandler(value = MismatchTokenTypeException.class)
    public ResponseEntity<CommonResponse> handleMismatchTokenTypeException(MismatchTokenTypeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handleMismatchTokenTypeException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }

    @ExceptionHandler(value = NotFoundElementException.class)
    public ResponseEntity<CommonResponse> handleNotFoundElementException(NotFoundElementException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.error("[handleNotFoundElementException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }

    @ExceptionHandler(value = NotRegisteredException.class)
    public ResponseEntity<CommonResponse> handleNotRegisteredException(NotRegisteredException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handleNotRegisteredException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }

    @ExceptionHandler(value = PortalLoginException.class)
    public ResponseEntity<CommonResponse> handlePortalLoginException(PortalLoginException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handlePortalLoginException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }

    @ExceptionHandler(value = NotOwnerException.class)
    public ResponseEntity<CommonResponse> handleNotOwnerException(NotOwnerException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handleNotOwnerException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }
}