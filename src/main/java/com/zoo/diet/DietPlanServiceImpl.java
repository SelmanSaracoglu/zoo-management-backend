// src/main/java/com/zoo/diet/DietPlanServiceImpl.java
package com.zoo.diet;

import com.zoo.animal.AnimalEntity; // senin Animal package'ına göre düzelt
import com.zoo.animal.AnimalRepository; // senin repo yoluna göre düzelt
import com.zoo.error.BadRequestException;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException; // varsa; yoksa RuntimeException at
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DietPlanServiceImpl implements DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final AnimalRepository animalRepository;

    public DietPlanServiceImpl(DietPlanRepository dietPlanRepository, AnimalRepository animalRepository) {
        this.dietPlanRepository = dietPlanRepository;
        this.animalRepository = animalRepository;
    }

    // ---- helpers ----
    private static LocalDate INF()
    { return LocalDate.of(9999, 12, 31); }

    /** [aStart,aEnd] ile [bStart,bEnd] aralıkları kesişiyor mu? (NULL end = sonsuz) */
    private static boolean overlaps(LocalDate aStart, LocalDate aEnd,
                                    LocalDate bStart, LocalDate bEnd) {
        LocalDate aE = (aEnd == null) ? INF() : aEnd;
        LocalDate bE = (bEnd == null) ? INF() : bEnd;
        // [aStart, aE] ile [bStart, bE] kesişiyor mu?
        return !aE.isBefore(bStart) && !bE.isBefore(aStart);
    }

    /** Aynı hayvan için tarih aralığı çakışması var mı? (UPDATE’te mevcut kaydı hariç tut) */
    private void ensureNoOverlap(Long animalId, LocalDate newStart, LocalDate newEnd, Long excludeId) {
        List<DietPlanEntity> plans = dietPlanRepository.findByAnimal_Id(animalId);
        for (DietPlanEntity dp : plans) {
            if (excludeId != null && dp.getId().equals(excludeId)) continue;
            if (overlaps(newStart, newEnd, dp.getStartDate(), dp.getEndDate())) {
                throw new ConflictException("Overlapping diet plan for the given date range");
            }
        }
    }

    // ---------- API ----------
    @Override
    public DietPlanEntity createForAnimal(Long animalId, DietPlanEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

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

        // 409 — tarih çakışması
        ensureNoOverlap(animalId, body.getStartDate(), body.getEndDate(), null);

        DietPlanEntity entity = new DietPlanEntity();
        entity.setAnimal(animal);
        entity.setCaloriesKcal(body.getCaloriesKcal());
        entity.setNotes(body.getNotes());
        entity.setStartDate(body.getStartDate());
        entity.setEndDate(body.getEndDate());

        return dietPlanRepository.save(entity);
    }

    @Override @Transactional(readOnly = true)
    public Page<DietPlanEntity> findByAnimal(Long animalId, Pageable pageable) {
        return dietPlanRepository.findByAnimal_Id(animalId, pageable);
    }

    @Override @Transactional(readOnly = true)
    public List<DietPlanEntity> findByAnimal(Long animalId) {
        return dietPlanRepository.findByAnimal_Id(animalId);
    }

    @Override @Transactional(readOnly = true)
    public Optional<DietPlanEntity> latestByAnimal(Long animalId) {
        return dietPlanRepository.findTopByAnimal_IdOrderByStartDateDesc(animalId);
    }

    @Override @Transactional(readOnly = true)
    public Optional<DietPlanEntity> findById(Long id) {
        return dietPlanRepository.findById(id);
    }

    @Override
    public Optional<DietPlanEntity> update(Long planId, DietPlanEntity body) {
        return dietPlanRepository.findById(planId).map(existing -> {
            Long animalId = existing.getAnimal().getId();

            // 400 — doğrulamalar
            if (body.getStartDate() == null || body.getEndDate() == null) {
                throw new BadRequestException("startDate and endDate must not be null");
            }
            if (body.getStartDate().isAfter(body.getEndDate())) {
                throw new BadRequestException("startDate must be before or equal to endDate");
            }
            if (body.getCaloriesKcal() != null && body.getCaloriesKcal() <= 0) {
                throw new BadRequestException("caloriesKcal must be > 0 when provided");
            }

            // 409 — kendisi hariç çakışma
            ensureNoOverlap(animalId, body.getStartDate(), body.getEndDate(), planId);

            // güncelle
            existing.setStartDate(body.getStartDate());
            existing.setEndDate(body.getEndDate());
            existing.setCaloriesKcal(body.getCaloriesKcal());
            existing.setNotes(body.getNotes());
            return dietPlanRepository.save(existing);
        }).or(() -> {
            throw new NotFoundException("Diet plan not found: " + planId);
        });
    }

    @Override
    public void delete(Long planId) {
        dietPlanRepository.deleteById(planId);
    }
}
