package org.medical.clinic.medicalclinic.DTO;

import org.springframework.validation.FieldError;

public record ValidationErrorData(String field, String message) {
    public ValidationErrorData(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}