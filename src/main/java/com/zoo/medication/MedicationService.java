package com.zoo.medication;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final AnimalRepository animalRepository;

    private static final java.util.Set<String> ALLOWED_ROUTES =
            new java.util.HashSet<String>(java.util.Arrays.asList("ORAL", "IM", "SC", "TOPICAL"));

    public MedicationService( MedicationRepository medicationRepository, AnimalRepository animalRepository){
        this.medicationRepository = medicationRepository;
        this.animalRepository = animalRepository;
    }

    public MedicationEntity createForAnimal(Long animalId, MedicationEntity body){
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(()-> new NotFoundException("Animal not found: " + animalId));

        if (body.getMedName() == null || body.getMedName().isBlank()) {
            throw new ConflictException("medName must not be blank");
        }
        if (body.getGivenAt() == null) {
            throw new ConflictException("givenAt must not be null");
        }

        String route = body.getRoute();
        if (route != null) {
            String normalized = route.trim().toUpperCase();
            if (!normalized.isEmpty() && !ALLOWED_ROUTES.contains(normalized)) {
                throw new com.zoo.error.ConflictException("route must be one of: ORAL, IM, SC, TOPICAL");
            }
            // normalize ederek kaydetmek istersen:
            body.setRoute(normalized.isEmpty() ? null : normalized);
        }

        if (medicationRepository.existsByAnimal_IdAndMedNameIgnoreCaseAndGivenAt(
                animalId, body.getMedName(), body.getGivenAt())) {
            throw new ConflictException("Duplicate medication at " + body.getGivenAt());
        }

        MedicationEntity entity = new MedicationEntity();
        entity.setAnimal(animal);                 // FK bağla
        entity.setMedName(body.getMedName());
        entity.setDose(body.getDose());
        entity.setRoute(body.getRoute());
        entity.setGivenAt(body.getGivenAt());
        entity.setReason(body.getReason());
        return medicationRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<MedicationEntity> listByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
        throw new NotFoundException("Animal not found: " + animalId);
        }
        return medicationRepository.findByAnimal_IdOrderByGivenAtDesc(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<MedicationEntity> latestByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return medicationRepository.findFirstByAnimal_IdOrderByGivenAtDesc(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<MedicationEntity> findById(Long medId) {
        return medicationRepository.findById(medId);
    }

    public Optional<MedicationEntity> update(Long medId, MedicationEntity body) {
        return medicationRepository.findById(medId).map(existing -> {
            Long animalId = existing.getAnimal().getId();

            if (body.getMedName() == null || body.getMedName().isBlank()) {
                throw new ConflictException("medName must not be blank");
            }
            if (body.getGivenAt() == null) {
                throw new ConflictException("givenAt must not be null");
            }

            String route2 = body.getRoute();
            if (route2 != null) {
                String normalized2 = route2.trim().toUpperCase();
                if (!normalized2.isEmpty() && !ALLOWED_ROUTES.contains(normalized2)) {
                    throw new com.zoo.error.ConflictException("route must be one of: ORAL, IM, SC, TOPICAL");
                }
                body.setRoute(normalized2.isEmpty() ? null : normalized2);
            }

            if (medicationRepository
                    .existsByAnimal_IdAndMedNameIgnoreCaseAndGivenAtAndIdNot(
                            animalId, body.getMedName(), body.getGivenAt(), medId)) {
                throw new ConflictException("Duplicate medication at " + body.getGivenAt());
            }

            // animal sabit
            existing.setMedName(body.getMedName());
            existing.setDose(body.getDose());
            existing.setRoute(body.getRoute());
            existing.setGivenAt(body.getGivenAt());
            existing.setReason(body.getReason());
            return medicationRepository.save(existing);
        }).or(()-> {
            throw new NotFoundException("Medication not found: " + medId);
        });
    }

    @Transactional
    public void delete(Long medId) {
        medicationRepository.deleteById(medId);
    }

}
