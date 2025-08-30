package com.zoo.vet;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VetVisitRepository extends JpaRepository<VetVisitEntity, Long> {

    List<VetVisitEntity> findByAnimal_IdOrderByVisitTimeDesc(Long animalId);

    List<VetVisitEntity> findByAnimal_IdAndVisitTimeBetweenOrderByVisitTimeDesc(
            Long animalId, LocalDateTime from, LocalDateTime to
    );

    // VetVisitRepository.java
    boolean existsByAnimal_IdAndVisitTime(Long animalId, java.time.LocalDateTime visitTime);

    // (update için, mevcut kaydı hariç tutmak üzere)
    boolean existsByAnimal_IdAndVisitTimeAndIdNot(Long animalId, java.time.LocalDateTime visitTime, Long id);

    boolean existsByAnimal_IdAndVisitTimeBetween(Long animalId,
                                                 java.time.LocalDateTime from, java.time.LocalDateTime to);

    boolean existsByAnimal_IdAndVisitTimeBetweenAndIdNot(Long animalId,
                                                         java.time.LocalDateTime from, java.time.LocalDateTime to, Long id);

    Optional<VetVisitEntity> findFirstByAnimal_IdOrderByVisitTimeDesc(Long animalId);

}
