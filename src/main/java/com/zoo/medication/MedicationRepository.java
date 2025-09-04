package com.zoo.medication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MedicationRepository extends JpaRepository<MedicationEntity, Long>,
        JpaSpecificationExecutor<MedicationEntity> {

}
