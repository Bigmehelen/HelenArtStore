package com.helenartstore.HelenArtStore.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 400,
                                "error", "Malformed JSON Request",
                                "message", "The request body is unreadable or malformed. Check your syntax."));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
                List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                                .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 400,
                                "error", "Validation Failed",
                                "messages", errors));
        }

        @ExceptionHandler(BindException.class)
        public ResponseEntity<Map<String, Object>> handleBindException(BindException ex) {
                List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                                .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 400,
                                "error", "Binding Failed",
                                "messages", errors));
        }

        @ExceptionHandler(UserAlreadyArtistException.class)
        public ResponseEntity<Map<String, Object>> handleUserAlreadyArtist(UserAlreadyArtistException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 409,
                                "error", "Conflict",
                                "message", ex.getMessage()));
        }

        @ExceptionHandler(ArtistNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleArtistNotFound(ArtistNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 404,
                                "error", "Not Found",
                                "message", ex.getMessage()));
        }

        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 404,
                                "error", "Not Found",
                                "message", ex.getMessage()));
        }

        @ExceptionHandler(UnauthorizedArtworkCreationException.class)
        public ResponseEntity<Map<String, Object>> handleUnauthorizedArtworkCreation(
                        UnauthorizedArtworkCreationException ex) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 403,
                                "error", "Forbidden",
                                "message", ex.getMessage()));
        }

        @ExceptionHandler(UserAlreadyExistException.class)
        public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 409,
                                "error", "Conflict",
                                "message", ex.getMessage()));
        }

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<Map<String, Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 401,
                                "error", "Unauthorized",
                                "message", ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
                log.error("Unhandled exception occurred: ", ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                                "timestamp", LocalDateTime.now().toString(),
                                "status", 500,
                                "error", "Internal Server Error",
                                "message",
                                ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred."));
        }
}
