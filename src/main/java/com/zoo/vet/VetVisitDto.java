package com.zoo.vet;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class VetVisitDto {
    private Long id;

    @NotNull
    private Long animalId;

    @NotNull
    private LocalDateTime visitTime;

    @Size(max = 120)
    private String purpose;

    @Size(max = 120)
    private String diagnosis;

    @Size(max = 255)
    private String treatment;

    private LocalDate followUpOn;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
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
