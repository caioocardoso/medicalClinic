package org.medical.clinic.medicalclinic.models;

import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.medical.clinic.medicalclinic.DTO.DoctorRegistrationData;
import org.medical.clinic.medicalclinic.DTO.PatientRegistrationData;
import org.medical.clinic.medicalclinic.DTO.UserRegistrationData;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Table(name = "users")
@Entity(name = "User")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    private String password;
    private String name;
    private String phone;

    @Embedded
    private Address address;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<RoleType> roleTypes = new HashSet<>();

    public User(){}

    public User(UserRegistrationData data, String hashedPassword) {
        this.password = hashedPassword;
        this.name = data.getName();
        this.email = data.getEmail();
        this.phone = data.getPhone();
        this.address = new Address(data.getAddress());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleTypes.stream()
                .map(roleType -> new SimpleGrantedAuthority(roleType.name()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public Long getId() {
        return id;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<RoleType> getRoles() {
        return roleTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setRoles(Set<RoleType> roleTypes) {
        this.roleTypes = roleTypes;
    }
}
