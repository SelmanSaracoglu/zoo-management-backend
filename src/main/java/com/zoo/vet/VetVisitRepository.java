package com.zoo.vet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VetVisitRepository extends JpaRepository<VetVisitEntity, Long>,
        JpaSpecificationExecutor<VetVisitEntity> {
}
