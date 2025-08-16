package com.zoo.weight;


import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "weight_log")
public class WeightLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weight_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalEntity animal;

    @Column(name = "measured_at", nullable = false)
    private LocalDateTime measuredAt;

    @Column(name = "weight_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal weightKg;

    protected WeightLogEntity() { }

    // getters / setters (id setter yok)
    public Long getId() { return id; }
    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }
    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }

}
