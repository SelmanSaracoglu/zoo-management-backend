package com.zoo.medication;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final AnimalRepository animalRepository;

    public MedicationService( MedicationRepository medicationRepository, AnimalRepository animalRepository){
        this.medicationRepository = medicationRepository;
        this.animalRepository = animalRepository;
    }

    public MedicationEntity createForAnimal(Long animalId, MedicationEntity body){
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(()-> new IllegalArgumentException("Animal not found: " + animalId));

        MedicationEntity entity = new MedicationEntity();
        entity.setAnimal(animal);                 // FK bağla
        entity.setMedName(body.getMedName());
        entity.setDose(body.getDose());
        entity.setRoute(body.getRoute());
        entity.setGivenAt(body.getGivenAt());
        entity.setReason(body.getReason());
        return medicationRepository.save(entity);
    }

    public List<MedicationEntity> listByAnimal(Long animalId) {
        return medicationRepository.findByAnimal_IdOrderByGivenAtDesc(animalId);
    }

    public List<MedicationEntity> listByAnimalAndRange(Long animalId, LocalDateTime from, LocalDateTime to) {
        return medicationRepository.findByAnimal_IdAndGivenAtBetweenOrderByGivenAtDesc(animalId, from, to);
    }

    public Optional<MedicationEntity> findById(Long medId) {
        return medicationRepository.findById(medId);
    }

    public Optional<MedicationEntity> update(Long medId, MedicationEntity body) {
        return medicationRepository.findById(medId).map(existing -> {
            // animal sabit
            existing.setMedName(body.getMedName());
            existing.setDose(body.getDose());
            existing.setRoute(body.getRoute());
            existing.setGivenAt(body.getGivenAt());
            existing.setReason(body.getReason());
            return medicationRepository.save(existing);
        });
    }

    public void delete(Long medId) {
        medicationRepository.deleteById(medId);
    }

}
