package com.zoo.health;

import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_check")
public class HealthCheckEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalEntity animal;

    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;

    @Column(name = "heart_rate_bpm")
    private Integer heartRateBpm;

    @Column(name = "temperature_c", precision = 4, scale = 1)
    private BigDecimal temperatureC; // örn: 38.6

    @Column(name = "respiration_rpm")
    private Integer respirationRpm;

    @Column(length = 100)
    private String condition;

    @Column(length = 255)
    private String notes;

    protected HealthCheckEntity() { }

    // --- getters/setters ---
    public Long getId() { return id; } // id setter yok
    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }
    public LocalDateTime getCheckTime() { return checkTime; }
    public void setCheckTime(LocalDateTime checkTime) { this.checkTime = checkTime; }
    public Integer getHeartRateBpm() { return heartRateBpm; }
    public void setHeartRateBpm(Integer heartRateBpm) { this.heartRateBpm = heartRateBpm; }
    public BigDecimal getTemperatureC() { return temperatureC; }
    public void setTemperatureC(BigDecimal temperatureC) { this.temperatureC = temperatureC; }
    public Integer getRespirationRpm() { return respirationRpm; }
    public void setRespirationRpm(Integer respirationRpm) { this.respirationRpm = respirationRpm; }
    public String getCondition() { return condition; }
    public void setCondition(String condition) { this.condition = condition; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
