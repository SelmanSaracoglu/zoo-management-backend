package com.zoo.staff;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "staff")
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long id;

    @Column(name = "full_name", nullable = false, length = 80)
    private String name;

    @Column(nullable = false, length = 40)
    private String role; // FE enum değerleri: KEEPER, VET, ADMIN

    @Column(length = 120)
    private String email;

    @Column(length = 40)
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "staff_animal",
            joinColumns = @JoinColumn(name = "staff_id"),
            inverseJoinColumns = @JoinColumn(name = "animal_id")
    )
    @JsonIgnore // entity JSON'ında ilişkiyi gizleyeceğiz; DTO’da id'leri döneriz
    private Set<AnimalEntity> animals = new HashSet<>();

    protected StaffEntity() {}

    // getters/setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Set<AnimalEntity> getAnimals() { return animals; }
}
