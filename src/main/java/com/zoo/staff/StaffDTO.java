package com.zoo.staff;

import java.util.List;

public class StaffDTO {
    private Long id;
    private String name;
    private String role;
    private String email;
    private String phone;
    private List<Long> animalIds;

    public StaffDTO() {}
    public StaffDTO(Long id, String name, String role, String email, String phone, List<Long> animalIds) {
        this.id = id; this.name = name; this.role = role; this.email = email; this.phone = phone; this.animalIds = animalIds;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public List<Long> getAnimalIds() { return animalIds; }
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAnimalIds(List<Long> animalIds) { this.animalIds = animalIds; }
}
