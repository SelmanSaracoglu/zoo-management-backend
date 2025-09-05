// src/main/java/com/zoo/weight/dto/WeightLogDto.java
package com.zoo.weight;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WeightLogDto {
    private Long id;

    @NotNull
    private Long animalId;

    @NotNull
    private LocalDateTime measuredAt;

    @NotNull @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal weightKg;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAnimalId() { return animalId; }
    public void setAnimalId(Long animalId) { this.animalId = animalId; }
    public LocalDateTime getMeasuredAt() { return measuredAt; }
    public void setMeasuredAt(LocalDateTime measuredAt) { this.measuredAt = measuredAt; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal weightKg) { this.weightKg = weightKg; }
}
