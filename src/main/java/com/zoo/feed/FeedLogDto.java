package com.zoo.feed;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class FeedLogDto {
    private Long id;

    @NotNull
    private Long animalId;

    @NotNull
    private LocalDateTime feedTime;

    @NotBlank @Size(max = 100)
    private String foodItem;

    @Positive
    private Integer quantityGrams;      // nullable ama >0 ise geçerli

    @Positive
    private Integer waterMilliliters;   // nullable ama >0 ise geçerli

    @Size(max=255)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public LocalDateTime getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(LocalDateTime feedTime) {
        this.feedTime = feedTime;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public Integer getQuantityGrams() {
        return quantityGrams;
    }

    public void setQuantityGrams(Integer quantityGrams) {
        this.quantityGrams = quantityGrams;
    }

    public Integer getWaterMilliliters() {
        return waterMilliliters;
    }

    public void setWaterMilliliters(Integer waterMilliliters) {
        this.waterMilliliters = waterMilliliters;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
