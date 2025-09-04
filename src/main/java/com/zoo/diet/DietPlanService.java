// src/main/java/com/zoo/diet/DietPlanService.java
package com.zoo.diet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DietPlanService {
    DietPlanEntity createForAnimal(Long animalId, DietPlanEntity toCreate);
    Page<DietPlanEntity> findByAnimal(Long animalId, Pageable pageable);
    List<DietPlanEntity> findByAnimal(Long animalId);
    Optional<DietPlanEntity> latestByAnimal(Long animalId);
    Optional<DietPlanEntity> findById(Long id);
    Optional<DietPlanEntity> update(Long planId, DietPlanEntity incoming);
    void delete(Long id);
}
