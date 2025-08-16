package com.zoo.staff;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long id;

    @Column(name = "full_name", nullable = false, length = 80)
    private String fullName;

    @Column(nullable = false, length = 40)
    private String role; // Keeper, Vet, Technician ...

    // JPA requires a no-args constructor
    protected StaffEntity() { }

    // Optional convenience constructor for required fields
    public StaffEntity(String fullName, String role) {
        this.fullName = fullName;
        this.role = role;
    }

    // Getters (no setter for id)
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }

    // Setters for mutable fields
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setRole(String role) { this.role = role; }
}
