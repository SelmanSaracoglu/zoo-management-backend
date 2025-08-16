package com.zoo.medication;


import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "medication")
public class MedicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "med_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalEntity animal;

    @Column(name = "med_name", length = 100, nullable = false)
    private String medName;

    @Column(length = 50)
    private String dose;

    @Column(length = 30)
    private String route;    // oral, IM, SC, topical

    @Column(name = "given_at", nullable = false)
    private LocalDateTime givenAt;

    @Column(length = 120)
    private String reason;

    protected MedicationEntity() { }

    // getters / setters (id setter yok)
    public Long getId() { return id; }
    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }
    public String getMedName() { return medName; }
    public void setMedName(String medName) { this.medName = medName; }
    public String getDose() { return dose; }
    public void setDose(String dose) { this.dose = dose; }
    public String getRoute() { return route; }
    public void setRoute(String route) { this.route = route; }
    public LocalDateTime getGivenAt() { return givenAt; }
    public void setGivenAt(LocalDateTime givenAt) { this.givenAt = givenAt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
