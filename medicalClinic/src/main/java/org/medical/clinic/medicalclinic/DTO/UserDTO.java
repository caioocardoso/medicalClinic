package org.medical.clinic.medicalclinic.DTO;

import org.medical.clinic.medicalclinic.models.User;

public record UserDTO(Long id, String email, String name, String phone) {
    public UserDTO(User user) {
        this(user.getId(), user.getEmail(), user.getName(), user.getPhone());
    }
    public UserDTO(UserDTO userDTO){
        this(userDTO.id(), userDTO.email(), userDTO.name(), userDTO.phone());
    }
}
