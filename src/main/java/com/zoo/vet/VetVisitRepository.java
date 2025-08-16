package com.zoo.vet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VetVisitRepository extends JpaRepository<VetVisitEntity, Long> {

    List<VetVisitEntity> findByAnimal_IdOrderByVisitTimeDesc(Long animalId);

    List<VetVisitEntity> findByAnimal_IdAndVisitTimeBetweenOrderByVisitTimeDesc(
            Long animalId, LocalDateTime from, LocalDateTime to
    );
}
