package org.medical.clinic.medicalclinic.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.medical.clinic.medicalclinic.models.Address;

public class UserRegistrationData {
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "A senha é obrigatória")
    private String password;
    @NotBlank(message = "O nome é obrigatório")
    private String name;
    @NotBlank(message = "O telefone é obrigatório")
    @Pattern(regexp = "^\\+?[0-9\\s()\\-]{8,20}$", message = "Telefone inválido. Use formato: (11) 98765-4321 ou +55 11 98765-4321")
    private String phone;
    @NotNull(message = "O endereço é obrigatório")
    @Valid
    private Address address;

    public UserRegistrationData() {}

    public UserRegistrationData(UserRegistrationData userRegistrationData) {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
