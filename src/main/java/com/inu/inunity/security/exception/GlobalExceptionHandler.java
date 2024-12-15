package com.inu.inunity.security.exception;

import com.inu.inunity.common.CommonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
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

    @ExceptionHandler(value = NotInformationMajorException.class)
    public ResponseEntity<CommonResponse> handleNotInformationMajorException(NotInformationMajorException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handleNotInformationMajorException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }

    @ExceptionHandler(value = NotSchoolEmailException.class)
    public ResponseEntity<CommonResponse> handleNotSchoolEmailException(NotSchoolEmailException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.error("[handleNotSchoolEmailException] {}", ex.getMessage());
        return ResponseEntity.status(status).body(CommonResponse.error(status, ex.getMessage(), null));
    }
}