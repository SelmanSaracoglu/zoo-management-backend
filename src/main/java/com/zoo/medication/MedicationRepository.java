package com.zoo.medication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {
    List<MedicationEntity> findByAnimal_IdOrderByGivenAtDesc(Long animalId);

    List<MedicationEntity> findByAnimal_IdAndGivenAtBetweenOrderByGivenAtDesc(
            Long animalId, LocalDateTime from, LocalDateTime to
    );
}
