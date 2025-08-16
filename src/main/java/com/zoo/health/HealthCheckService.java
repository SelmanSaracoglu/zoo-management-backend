package com.zoo.health;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HealthCheckService {

    private final HealthCheckRepository healthCheckRepository;
    private final AnimalRepository animalRepository;

    public HealthCheckService(HealthCheckRepository healthCheckRepository,
                              AnimalRepository animalRepository) {
        this.healthCheckRepository = healthCheckRepository;
        this.animalRepository = animalRepository;
    }

    public HealthCheckEntity createForAnimal(Long animalId, HealthCheckEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));

        HealthCheckEntity entity = new HealthCheckEntity();
        entity.setAnimal(animal);
        entity.setCheckTime(body.getCheckTime());
        entity.setHeartRateBpm(body.getHeartRateBpm());
        entity.setTemperatureC(body.getTemperatureC());
        entity.setRespirationRpm(body.getRespirationRpm());
        entity.setCondition(body.getCondition());
        entity.setNotes(body.getNotes());

        return healthCheckRepository.save(entity);
    }
    public List<HealthCheckEntity> listByAnimal(Long animalId) {
        return healthCheckRepository.findByAnimal_IdOrderByCheckTimeDesc(animalId);
    }

    public List<HealthCheckEntity> listByAnimalAndRange(Long animalId, LocalDateTime from, LocalDateTime to) {
        return healthCheckRepository.findByAnimal_IdAndCheckTimeBetweenOrderByCheckTimeDesc(animalId, from, to);
    }

    public Optional<HealthCheckEntity> findById(Long checkId) {
        return healthCheckRepository.findById(checkId);
    }

    public Optional<HealthCheckEntity> update(Long checkId, HealthCheckEntity body) {
        return healthCheckRepository.findById(checkId).map(existing -> {
            // animal sabit kalır
            existing.setCheckTime(body.getCheckTime());
            existing.setHeartRateBpm(body.getHeartRateBpm());
            existing.setTemperatureC(body.getTemperatureC());
            existing.setRespirationRpm(body.getRespirationRpm());
            existing.setCondition(body.getCondition());
            existing.setNotes(body.getNotes());
            return healthCheckRepository.save(existing);
        });
    }

    public void delete(Long checkId) {
        healthCheckRepository.deleteById(checkId);
    }

}
