package com.zoo.diet;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.BadRequestException;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    // 400 — alan doğrulamaları
    if (body.getStartDate() == null || body.getEndDate() == null) {
        throw new BadRequestException("startDate and endDate must not be null");
    }
    if (body.getStartDate().isAfter(body.getEndDate())) {
        throw new BadRequestException("startDate must be before or equal to endDate");
    }
    if (body.getCaloriesKcal() != null && body.getCaloriesKcal() <= 0) {
        throw new BadRequestException("caloriesKcal must be > 0 when provided");
    }

    // 409 — aralık çakışması (aynı hayvan)
    LocalDate newStart = body.getStartDate();
    LocalDate newEnd = body.getEndDate();
    if (dietPlanRepository.existsByAnimal_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            animalId, newEnd, newStart)) {
        throw new ConflictException("Overlapping diet plan for the given date range");
    }

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

    @Transactional(readOnly = true)
    public List<DietPlanEntity> listByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return dietPlanRepository.findByAnimal_IdAndEndDateIsNull(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<DietPlanEntity> latestByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return dietPlanRepository.findFirstByAnimal_IdOrderByStartDateDesc(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<DietPlanEntity> findById(Long planId) {
        return dietPlanRepository.findById(planId);
    }

    @Transactional
    public Optional<DietPlanEntity> update(Long planId, DietPlanEntity body) {
        return dietPlanRepository.findById(planId).map(existing -> {
            Long animalId = existing.getAnimal().getId();

            // 400
            if (body.getStartDate() == null || body.getEndDate() == null) {
                throw new BadRequestException("startDate and endDate must not be null");
            }
            if (body.getStartDate().isAfter(body.getEndDate())) {
                throw new BadRequestException("startDate must be before or equal to endDate");
            }
            if (body.getCaloriesKcal() != null && body.getCaloriesKcal() <= 0) {
                throw new BadRequestException("caloriesKcal must be > 0 when provided");
            }

            // 409 - kendisi hariç çakışma
            if (dietPlanRepository
                    .existsByAnimal_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndIdNot(
                            animalId, body.getEndDate(), body.getStartDate(), planId)) {
                throw new ConflictException("Overlapping diet plan for the given date range");
            }

            existing.setStartDate(body.getStartDate());
            existing.setEndDate(body.getEndDate());
            existing.setCaloriesKcal(body.getCaloriesKcal());
            existing.setNotes(body.getNotes());
            return dietPlanRepository.save(existing);
        }).or(() -> {
            throw new NotFoundException("Diet plan not found: " + planId);
        });
    }

    @Transactional
    public void delete(Long planId) {
        dietPlanRepository.deleteById(planId);
    }

}
