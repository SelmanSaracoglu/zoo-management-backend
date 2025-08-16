package com.zoo.vet;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VetVisitService {

    private final VetVisitRepository vetVisitRepository;
    private final AnimalRepository animalRepository;

    public VetVisitService(VetVisitRepository vetVisitRepository,
                           AnimalRepository animalRepository) {
        this.vetVisitRepository = vetVisitRepository;
        this.animalRepository = animalRepository;
    }

    public VetVisitEntity createForAnimal(Long animalId, VetVisitEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));

        VetVisitEntity entity = new VetVisitEntity();
        entity.setAnimal(animal); // FK bağla
        entity.setVisitTime(body.getVisitTime());
        entity.setPurpose(body.getPurpose());
        entity.setDiagnosis(body.getDiagnosis());
        entity.setTreatment(body.getTreatment());
        entity.setFollowUpOn(body.getFollowUpOn());
        return vetVisitRepository.save(entity);
    }

    public List<VetVisitEntity> listByAnimal(Long animalId) {
        return vetVisitRepository.findByAnimal_IdOrderByVisitTimeDesc(animalId);
    }

    public List<VetVisitEntity> listByAnimalAndRange(Long animalId, LocalDateTime from, LocalDateTime to) {
        return vetVisitRepository.findByAnimal_IdAndVisitTimeBetweenOrderByVisitTimeDesc(animalId, from, to);
    }

    public Optional<VetVisitEntity> findById(Long visitId) {
        return vetVisitRepository.findById(visitId);
    }

    public Optional<VetVisitEntity> update(Long visitId, VetVisitEntity body) {
        return vetVisitRepository.findById(visitId).map(existing -> {
            // animal sabit
            existing.setVisitTime(body.getVisitTime());
            existing.setPurpose(body.getPurpose());
            existing.setDiagnosis(body.getDiagnosis());
            existing.setTreatment(body.getTreatment());
            existing.setFollowUpOn(body.getFollowUpOn());
            return vetVisitRepository.save(existing);
        });
    }

    public void delete(Long visitId) {
        vetVisitRepository.deleteById(visitId);
    }
}
