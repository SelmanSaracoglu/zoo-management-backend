package com.zoo.diet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlanEntity, Long> {

    List<DietPlanEntity> findByAnimal_Id(Long animalId);

    List<DietPlanEntity> findByAnimal_IdAndEndDateIsNull(Long animalId);
}
