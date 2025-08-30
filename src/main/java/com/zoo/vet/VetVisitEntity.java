package com.zoo.vet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vet_visit")
public class VetVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    @JsonIgnore // ❗ JSON’a animal nesnesini dahil etme (döngüyü kes)
    private AnimalEntity animal;

    @Column(name = "visit_time", nullable = false)
    private LocalDateTime visitTime;

    @Column(length = 120)
    private String purpose;

    @Column(length = 120)
    private String diagnosis;

    @Column(length = 255)
    private String treatment;

    @Column(name = "follow_up_on")
    private LocalDate followUpOn;

    protected VetVisitEntity() { }

    // getters/setters (id setter yok)
    public Long getId() { return id; }
    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }
    public LocalDateTime getVisitTime() { return visitTime; }
    public void setVisitTime(LocalDateTime visitTime) { this.visitTime = visitTime; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public String getTreatment() { return treatment; }
    public void setTreatment(String treatment) { this.treatment = treatment; }
    public LocalDate getFollowUpOn() { return followUpOn; }
    public void setFollowUpOn(LocalDate followUpOn) { this.followUpOn = followUpOn; }
}
