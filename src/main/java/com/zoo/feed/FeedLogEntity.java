package com.zoo.feed;


import com.zoo.animal.AnimalEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "feed_log")
public class FeedLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private AnimalEntity animal;

    @Column(name = "feed_time", nullable = false)
    private LocalDateTime feedTime;

    @Column(name = "food_item", length = 100, nullable = false)
    private String foodItem;

    @Column(name = "quantity_g")
    private Integer quantityGrams; // nullable

    @Column(name = "water_ml")
    private Integer waterMilliliters; // nullable

    @Column(length = 255)
    private String notes;

    protected FeedLogEntity(){}

    // --- getters/setters ---
    public Long getId() { return id; } // id setter YOK
    public AnimalEntity getAnimal() { return animal; }
    public void setAnimal(AnimalEntity animal) { this.animal = animal; }
    public LocalDateTime getFeedTime() { return feedTime; }
    public void setFeedTime(LocalDateTime feedTime) { this.feedTime = feedTime; }
    public String getFoodItem() { return foodItem; }
    public void setFoodItem(String foodItem) { this.foodItem = foodItem; }
    public Integer getQuantityGrams() { return quantityGrams; }
    public void setQuantityGrams(Integer quantityGrams) { this.quantityGrams = quantityGrams; }
    public Integer getWaterMilliliters() { return waterMilliliters; }
    public void setWaterMilliliters(Integer waterMilliliters) { this.waterMilliliters = waterMilliliters; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

}
