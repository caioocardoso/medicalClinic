package org.medical.clinic.medicalclinic.DTO;

import org.medical.clinic.medicalclinic.models.User;

public record UserDTO(String username, String password) {
    public UserDTO(User saved) {
        this(saved.getUsername(), saved.getPassword());
    }
}
