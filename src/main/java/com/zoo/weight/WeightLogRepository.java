package com.zoo.weight;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface WeightLogRepository extends JpaRepository<WeightLogEntity, Long> {

    List<WeightLogEntity> findByAnimal_IdOrderByMeasuredAtDesc(Long animalId);

    List<WeightLogEntity> findByAnimal_IdAndMeasuredAtBetweenOrderByMeasuredAtDesc(
            Long animalId, LocalDateTime from, LocalDateTime to
    );
}
