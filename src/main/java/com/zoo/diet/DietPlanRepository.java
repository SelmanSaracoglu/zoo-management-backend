package com.zoo.diet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlanEntity, Long> {

    List<DietPlanEntity> findByAnimal_Id(Long animalId);

    List<DietPlanEntity> findByAnimal_IdAndEndDateIsNull(Long animalId);

    Optional<DietPlanEntity> findFirstByAnimal_IdOrderByStartDateDesc(Long animalId);

    boolean existsByAnimal_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long animalId, LocalDate newEnd, LocalDate newStart);

    boolean existsByAnimal_IdAndStartDateLessThanEqualAndEndDateGreaterThanEqualAndIdNot(
            Long animalId, LocalDate newEnd, LocalDate newStart, Long id);
}
