package org.medical.clinic.medicalclinic.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.medical.clinic.medicalclinic.DTO.ErrorResponseData;
import org.medical.clinic.medicalclinic.DTO.ValidationErrorData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseData> handleGenericException(Exception ex) {
        var errorData = new ErrorResponseData(
                "An error occurred",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.status(errorData.status()).body(errorData);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorData>> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors().stream()
                .map(ValidationErrorData::new)
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> handleEntityNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseData> handleBusinessLogicError(ResponseStatusException ex) {
        var errorData = new ErrorResponseData(
                "Error processing the request",
                ex.getReason(),
                ex.getStatusCode().value()
        );
        return ResponseEntity.status(errorData.status()).body(errorData);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseData> handleBadCredentials(BadCredentialsException ex) {
        var errorData = new ErrorResponseData(
                "Authentication failed",
                "Incorrect login or password",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(errorData.status()).body(errorData);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponseData> handleDisabledAccount(DisabledException ex) {
        var errorData = new ErrorResponseData(
                "Disabled account",
                "Your account has been disabled. Contact the support ",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(errorData.status()).body(errorData);
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponseData> handleLockedAccount(LockedException ex) {
        var errorData = new ErrorResponseData(
                "Blocked Account",
                "Your account is blocked. Please contact support.",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(errorData.status()).body(errorData);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseData> handleAuthenticationException(AuthenticationException ex) {
        var errorData = new ErrorResponseData(
                "Authentication error",
                "Unable to authenticate. Please check your credentials.",
                HttpStatus.UNAUTHORIZED.value()
        );
        return ResponseEntity.status(errorData.status()).body(errorData);
    }
}