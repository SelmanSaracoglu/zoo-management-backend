package com.zoo.diet;

import org.springframework.data.domain.Page;      // <-- DOĞRU import
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlanEntity, Long>,
        JpaSpecificationExecutor<DietPlanEntity> {

    org.springframework.data.domain.Page<DietPlanEntity> findByAnimal_Id(Long animalId, Pageable pageable);

    List<DietPlanEntity> findByAnimal_Id(Long animalId);

    Optional<DietPlanEntity> findTopByAnimal_IdOrderByStartDateDesc(Long animalId);

}
