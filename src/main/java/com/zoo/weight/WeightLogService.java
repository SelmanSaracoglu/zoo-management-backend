package com.zoo.weight;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WeightLogService {
    private final WeightLogRepository weightLogRepository;
    private final AnimalRepository animalRepository;

    public WeightLogService(WeightLogRepository weightLogRepository, AnimalRepository animalRepository){
        this.weightLogRepository = weightLogRepository;
        this.animalRepository = animalRepository;
    }

    public WeightLogEntity createForAnimal(Long animalId, WeightLogEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(()-> new IllegalArgumentException("Animal not found: " + animalId));

        WeightLogEntity entity = new WeightLogEntity();
        entity.setAnimal(animal);
        entity.setMeasuredAt(body.getMeasuredAt());
        entity.setWeightKg(body.getWeightKg());

        return weightLogRepository.save(entity);
    }

    public List<WeightLogEntity> listByAnimal(Long animalId) {
        return weightLogRepository.findByAnimal_IdOrderByMeasuredAtDesc(animalId);
    }

    public List<WeightLogEntity> listByAnimalAndRange(Long animalId, LocalDateTime from, LocalDateTime to) {
        return weightLogRepository.findByAnimal_IdAndMeasuredAtBetweenOrderByMeasuredAtDesc(animalId, from, to);
    }

    public Optional<WeightLogEntity> findById(Long weightId) {
        return weightLogRepository.findById(weightId);
    }

    public Optional<WeightLogEntity> update(Long weightId, WeightLogEntity body) {
        return weightLogRepository.findById(weightId).map(existing -> {
            // animal sabit
            existing.setMeasuredAt(body.getMeasuredAt());
            existing.setWeightKg(body.getWeightKg());
            return weightLogRepository.save(existing);
        });
    }

    public void delete(Long weightId) {
        weightLogRepository.deleteById(weightId);
    }

}
