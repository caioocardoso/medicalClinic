package org.medical.clinic.medicalclinic.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.medical.clinic.medicalclinic.DTO.ErrorResponseData;
import org.medical.clinic.medicalclinic.DTO.ValidationErrorData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {
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
                ex.getReason(),
                "Erro no processamento da solicitação",
                ex.getStatusCode().value()
        );
        return ResponseEntity.status(ex.getStatusCode()).body(errorData);
    }
}