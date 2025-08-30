package com.zoo.medication;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends JpaRepository<MedicationEntity, Long> {
    List<MedicationEntity> findByAnimal_IdOrderByGivenAtDesc(Long animalId);

    Optional<MedicationEntity> findFirstByAnimal_IdOrderByGivenAtDesc(Long animalId);

    boolean existsByAnimal_IdAndMedNameIgnoreCaseAndGivenAt(Long animalId, String medName, LocalDateTime givenAt);
    boolean existsByAnimal_IdAndMedNameIgnoreCaseAndGivenAtAndIdNot(Long animalId, String medName, LocalDateTime givenAt, Long id);
}
