package com.zoo.health;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheckEntity, Long> {
    List<HealthCheckEntity> findByAnimal_IdOrderByCheckTimeDesc(Long animalId);

    List<HealthCheckEntity> findByAnimal_IdAndCheckTimeBetweenOrderByCheckTimeDesc(
            Long animalId, LocalDateTime from, LocalDateTime to
    );
}
