package com.zoo.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class StaffDto {
    private Long id;

    @NotBlank @Size(max = 80)
    private String name;

    @NotBlank @Size(max = 40)
    private String role;  // KEEPER | VET | ADMIN (service’de whitelist)

    @Size(max=120) @Email
    private String email;

    @Size(max=40)
    private String phone;

    // Many-to-many relation as IDs
    private Set<Long> animalIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public Set<Long> getAnimalIds() {
        return animalIds;
    }

    public void setAnimalIds(Set<Long> animalIds) {
        this.animalIds = animalIds;
    }
}
