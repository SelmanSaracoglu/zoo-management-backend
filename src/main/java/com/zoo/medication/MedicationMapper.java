package com.zoo.medication;

public class MedicationMapper {
    private MedicationMapper() {}

    public static MedicationDto toDto(MedicationEntity e){
        MedicationDto d = new MedicationDto();
        d.setId(e.getId());
        d.setAnimalId(e.getAnimalId());// entity @JsonProperty ile expose ediyor
        d.setMedName(e.getMedName());
        d.setDose(e.getDose());
        d.setRoute(e.getRoute());
        d.setGivenAt(e.getGivenAt());
        d.setReason(e.getReason());
        return d;
    }

    public static void copyToEntity(MedicationDto d, MedicationEntity e){
        e.setMedName(d.getMedName());
        e.setDose(d.getDose());
        e.setRoute(d.getRoute());
        e.setGivenAt(d.getGivenAt());
        e.setReason(d.getReason());
    }
}
