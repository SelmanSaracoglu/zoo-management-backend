package com.zoo.diet;

public class DietPlanMapper {

    private DietPlanMapper() {}

    public static DietPlanDto toDto(DietPlanEntity e) {
        DietPlanDto d = new DietPlanDto();
        d.setId(e.getId());
        d.setAnimalId(e.getAnimal().getId());    // Animal relation varsayıldı
        d.setStartDate(e.getStartDate());
        d.setEndDate(e.getEndDate());
        d.setCaloriesKcal(e.getCaloriesKcal());
        d.setNotes(e.getNotes());
        return d;
    }

    public static DietPlanEntity toEntity(DietPlanDto d, DietPlanEntity target){
        // target: yeni ya da mevcut entity
        target.setStartDate(d.getStartDate());
        target.setEndDate(d.getEndDate());
        target.setCaloriesKcal(d.getCaloriesKcal());
        target.setNotes(d.getNotes());
        return target;
    }
}
