package com.zoo.vet;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

        //Check
        if (vetVisitRepository.existsByAnimal_IdAndVisitTime(animalId, body.getVisitTime())) {
            throw new ConflictException(
                    "Overlapping vet visit at " + body.getVisitTime()
            );
        }

        // ±30 dk kontrol
        java.time.LocalDateTime when = body.getVisitTime();
        java.time.LocalDateTime from = when.minusMinutes(30);
        java.time.LocalDateTime to   = when.plusMinutes(30);
        if (vetVisitRepository.existsByAnimal_IdAndVisitTimeBetween(animalId, from, to)) {
            throw new ConflictException("Another visit within ±30 minutes of " + when);
        }

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
        if (!animalRepository.existsById(animalId)) {
            throw new com.zoo.error.NotFoundException("Animal not found: " + animalId);
        }
        return vetVisitRepository.findByAnimal_IdOrderByVisitTimeDesc(animalId);
    }

    public List<VetVisitEntity> listByAnimalAndRange(Long animalId, LocalDateTime from, LocalDateTime to) {
        if (!animalRepository.existsById(animalId)) {
            throw new com.zoo.error.NotFoundException("Animal not found: " + animalId);
        }
        return vetVisitRepository.findByAnimal_IdAndVisitTimeBetweenOrderByVisitTimeDesc(animalId, from, to);
    }

    public Optional<VetVisitEntity> findById(Long visitId) {
        return vetVisitRepository.findById(visitId);
    }

    public Optional<VetVisitEntity> update(Long visitId, VetVisitEntity body) {
        return vetVisitRepository.findById(visitId).map(existing -> {
            Long animalId = existing.getAnimal().getId();

            if (vetVisitRepository.existsByAnimal_IdAndVisitTimeAndIdNot(
                    animalId, body.getVisitTime(), visitId)) {
                throw new ConflictException("Overlapping vet visit at " + body.getVisitTime());
            }

            // ±30 dk kontrol
            java.time.LocalDateTime when = body.getVisitTime();
            java.time.LocalDateTime from = when.minusMinutes(30);
            java.time.LocalDateTime to   = when.plusMinutes(30);
            if (vetVisitRepository.existsByAnimal_IdAndVisitTimeBetweenAndIdNot(
                    animalId, from, to, visitId)) {
                throw new ConflictException("Another visit within ±30 minutes of " + when);
            }

            // animal sabit
            existing.setVisitTime(body.getVisitTime());
            existing.setPurpose(body.getPurpose());
            existing.setDiagnosis(body.getDiagnosis());
            existing.setTreatment(body.getTreatment());
            existing.setFollowUpOn(body.getFollowUpOn());
            return vetVisitRepository.save(existing);
        }).or(() -> {
            throw new NotFoundException("Vet visit not found: " + visitId);
        });
    }

    @Transactional(readOnly = true)
    public Optional<VetVisitEntity> latestByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return vetVisitRepository.findFirstByAnimal_IdOrderByVisitTimeDesc(animalId);
    }
    public void delete(Long visitId) {
        vetVisitRepository.deleteById(visitId);
    }
}
