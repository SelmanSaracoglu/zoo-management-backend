package com.zoo.vet;

public class VetVisitMapper {
    private VetVisitMapper(){}

    public static VetVisitDto toDto(VetVisitEntity e){
        VetVisitDto d = new VetVisitDto();
        d.setId(e.getId());
        d.setAnimalId(e.getAnimal().getId());
        d.setVisitTime(e.getVisitTime());
        d.setPurpose(e.getPurpose());
        d.setDiagnosis(e.getDiagnosis());
        d.setTreatment(e.getTreatment());
        d.setFollowUpOn(e.getFollowUpOn());
        return d;
    }

    public static void copyToEntity(VetVisitDto d, VetVisitEntity e){
        e.setVisitTime(d.getVisitTime());
        e.setPurpose(d.getPurpose());
        e.setDiagnosis(d.getDiagnosis());
        e.setTreatment(d.getTreatment());
        e.setFollowUpOn(d.getFollowUpOn());
    }
}
