package com.zoo.diet;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "diet_plan")
public class DietPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    @JsonIgnore
    private AnimalEntity animal;

    @Column(name = "calories_kcal")
    private  Integer caloriesKcal;

    @Column(length = 255)
    private String notes;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    protected DietPlanEntity() { } // JPA



    // --- getters/setters ---
    public Long getId() { return id; } // id setter yok – tasarım gereği
    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }
    public Integer getCaloriesKcal() { return caloriesKcal; }
    public void setCaloriesKcal(Integer caloriesKcal) { this.caloriesKcal = caloriesKcal; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @JsonProperty("animalId")
    public Long getAnimalId() { return (animal != null) ? animal.getId() : null; }


}
