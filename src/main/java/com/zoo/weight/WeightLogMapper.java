package com.zoo.weight;

public class WeightLogMapper {
    private WeightLogMapper() {}

    public static WeightLogDto toDto(WeightLogEntity e){
        WeightLogDto d = new WeightLogDto();
        d.setId(e.getId());
        d.setAnimalId(e.getAnimal().getId());
        d.setMeasuredAt(e.getMeasuredAt());
        d.setWeightKg(e.getWeightKg());
        return d;
    }

    public static void copyToEntity(WeightLogDto d, WeightLogEntity e){
        e.setMeasuredAt(d.getMeasuredAt());
        e.setWeightKg(d.getWeightKg());
    }
}
