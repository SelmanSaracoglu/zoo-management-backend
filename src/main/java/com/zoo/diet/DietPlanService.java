package com.zoo.diet;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final AnimalRepository animalRepository;

    public DietPlanService(DietPlanRepository dietPlanRepository, AnimalRepository animalRepository) {
        this.dietPlanRepository = dietPlanRepository;
        this.animalRepository = animalRepository;
    }

public DietPlanEntity createForAnimal(Long animalId, DietPlanEntity body) {
    AnimalEntity animal = animalRepository.findById(animalId)
            .orElseThrow(() -> new IllegalArgumentException("Animal not found: " + animalId));

    DietPlanEntity entity = new DietPlanEntity();
    entity.setAnimal(animal);
    entity.setCaloriesKcal(body.getCaloriesKcal());
    entity.setNotes(body.getNotes());
    entity.setStartDate(body.getStartDate());
    entity.setEndDate(body.getEndDate());

    return dietPlanRepository.save(entity);
}

    public List<DietPlanEntity> findByAnimal(Long animalId) {
        return dietPlanRepository.findByAnimal_Id(animalId);
    }

    public Optional<DietPlanEntity> findById(Long planId) {
        return dietPlanRepository.findById(planId);
    }

    public Optional<DietPlanEntity> update(Long planId, DietPlanEntity body) {
        return dietPlanRepository.findById(planId).map(existing -> {
            // animal ilişkisini burada değiştirmiyoruz (stabil kalsın)
            existing.setCaloriesKcal(body.getCaloriesKcal());
            existing.setNotes(body.getNotes());
            existing.setStartDate(body.getStartDate());
            existing.setEndDate(body.getEndDate());
            return dietPlanRepository.save(existing);
        });
    }

    public void delete(Long planId) {
        dietPlanRepository.deleteById(planId);
    }

}
