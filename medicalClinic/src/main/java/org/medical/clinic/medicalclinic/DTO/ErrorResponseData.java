package org.medical.clinic.medicalclinic.DTO;

public record ErrorResponseData(String error, String message, Integer status) {}