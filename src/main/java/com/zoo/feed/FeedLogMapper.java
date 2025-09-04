package com.zoo.feed;

public class FeedLogMapper {
    private FeedLogMapper(){}

    public static FeedLogDto toDto(FeedLogEntity e){
        FeedLogDto d = new FeedLogDto();
        d.setId(e.getId());
        d.setAnimalId(e.getAnimal().getId());
        d.setFeedTime(e.getFeedTime());
        d.setFoodItem(e.getFoodItem());
        d.setQuantityGrams(e.getQuantityGrams());
        d.setWaterMilliliters(e.getWaterMilliliters());
        d.setNotes(e.getNotes());
        return d;
    }
    public static void copyToEntity(FeedLogDto d, FeedLogEntity e){
        e.setFeedTime(d.getFeedTime());
        e.setFoodItem(d.getFoodItem());
        e.setQuantityGrams(d.getQuantityGrams());
        e.setWaterMilliliters(d.getWaterMilliliters());
        e.setNotes(d.getNotes());
    }
}
