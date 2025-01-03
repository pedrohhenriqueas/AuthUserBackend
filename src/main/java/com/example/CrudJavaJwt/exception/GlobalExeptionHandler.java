package com.example.CrudJavaJwt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExeptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExeptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        HttpStatus status = determineHttpStatus(exception);
        logException(exception);
        return new ResponseEntity<>(new ApiException(exception.getMessage(), status.value()), status);
    }

    private HttpStatus determineHttpStatus(Exception exception) {
        return EXCEPTION_STATUS.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void logException(Exception exception) {
        StackTraceElement element = exception.getStackTrace()[0];
        logger.error("Exception occurred in [{}] at line [{}]: {}",
                element.getClassName(),
                element.getLineNumber(),
                exception.getMessage());
    }

    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS = new HashMap<>();

    static {
        // Map de exceções com os respectivos status HTTP
        EXCEPTION_STATUS.put(NoSuchElementException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(FileNotFoundException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(NoSuchFileException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(UsernameNotFoundException.class, HttpStatus.NOT_FOUND);

        EXCEPTION_STATUS.put(IllegalArgumentException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(InvalidDataAccessApiUsageException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(InvalidDataAccessResourceUsageException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(JpaObjectRetrievalFailureException.class, HttpStatus.BAD_REQUEST);
        EXCEPTION_STATUS.put(NullPointerException.class, HttpStatus.BAD_REQUEST);

        EXCEPTION_STATUS.put(AccessDeniedException.class, HttpStatus.FORBIDDEN);

        EXCEPTION_STATUS.put(IOException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        EXCEPTION_STATUS.put(RuntimeException.class, HttpStatus.INTERNAL_SERVER_ERROR);

        EXCEPTION_STATUS.put(EmptyListException.class, HttpStatus.NOT_FOUND);
        EXCEPTION_STATUS.put(NotAuthorizedException.class, HttpStatus.UNAUTHORIZED);
        EXCEPTION_STATUS.put(ConflictException.class, HttpStatus.CONFLICT);
        EXCEPTION_STATUS.put(UnsupportedOperationException.class, HttpStatus.NOT_IMPLEMENTED);
    }

}
