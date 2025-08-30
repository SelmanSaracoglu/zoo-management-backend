package com.zoo.weight;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.diet.DietPlanEntity;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
    public WeightLogEntity createForAnimal(Long animalId, WeightLogEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(()-> new IllegalArgumentException("Animal not found: " + animalId));

        if (body.getMeasuredAt() == null) {
            throw new ConflictException("measuredAt must not be null");
        }

        BigDecimal w = body.getWeightKg();
        if (w == null || w.compareTo(new BigDecimal("0.1")) < 0) {
            throw new ConflictException("weightKg must be >= 0.1 kg");
        }

        if (weightLogRepository.existsByAnimal_IdAndMeasuredAt(animalId, body.getMeasuredAt())) {
            throw new ConflictException("Overlapping weight log at " + body.getMeasuredAt());
        }

        WeightLogEntity entity = new WeightLogEntity();
        entity.setAnimal(animal);
        entity.setMeasuredAt(body.getMeasuredAt());
        entity.setWeightKg(body.getWeightKg());

        return weightLogRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<WeightLogEntity> listByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return weightLogRepository.findByAnimal_IdOrderByMeasuredAtDesc(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<WeightLogEntity> latestByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return weightLogRepository.findFirstByAnimal_IdOrderByMeasuredAtDesc(animalId);
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
