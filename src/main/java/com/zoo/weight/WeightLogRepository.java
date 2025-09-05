package com.zoo.weight;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeightLogRepository extends JpaRepository<WeightLogEntity, Long>, JpaSpecificationExecutor<WeightLogEntity> {

}
