package com.dau.file.exception;

import com.dau.file.exception.exception.TooManyRequestsException;
import com.dau.file.exception.exception.UnAuthenticatedException;
import com.dau.file.exception.exception.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class CommonRestExceptionHandler {

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = { UnAuthenticatedException.class, UsernameNotFoundException.class })
    public ResponseEntity unAuthenticatedException(UnAuthenticatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.getReasonPhrase(), e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = UnAuthorizedException.class)
    public ResponseEntity unAuthorizedException(UnAuthorizedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ExceptionResponse(HttpStatus.FORBIDDEN.getReasonPhrase(), e.getMessage()));
    }

    @ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(value = TooManyRequestsException.class)
    public ResponseEntity tooManyRequestsException(TooManyRequestsException e) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ExceptionResponse(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(), e.getMessage()));
    }

}
