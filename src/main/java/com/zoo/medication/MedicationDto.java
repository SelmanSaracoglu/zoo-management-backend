package com.zoo.medication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class MedicationDto {
    private Long id;

    @NotNull
    private Long animalId;

    @NotBlank
    @Size(max = 100)
    private String medName;

    @Size(max = 50)
    private String dose;

    @Size(max = 30)
    private String route;

    @NotNull
    private LocalDateTime givenAt;

    @Size(max = 120)
    private String reason;



    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Long getAnimalId() {return animalId;}
    public void setAnimalId(Long animalId) {this.animalId = animalId;}

    public String getMedName() {return medName;}
    public void setMedName(String medName) {this.medName = medName;}

    public String getDose() {return dose;}
    public void setDose(String dose) {this.dose = dose;}

    public String getRoute() {return route;}
    public void setRoute(String route) {this.route = route;}

    public LocalDateTime getGivenAt() {return givenAt;}
    public void setGivenAt(LocalDateTime givenAt) {this.givenAt = givenAt;}

    public String getReason() {return reason;}
    public void setReason(String reason) {this.reason = reason;}
}
