package com.zoo.weight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeightLogRepository extends JpaRepository<WeightLogEntity, Long> {

    List<WeightLogEntity> findByAnimal_IdOrderByMeasuredAtDesc(Long animalId);

    Optional<WeightLogEntity> findFirstByAnimal_IdOrderByMeasuredAtDesc(Long animalId);

    boolean existsByAnimal_IdAndMeasuredAt(Long animalId, LocalDateTime measuredAt);

}
