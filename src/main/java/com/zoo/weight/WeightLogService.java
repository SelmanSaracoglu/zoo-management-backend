// src/main/java/com/zoo/weight/WeightLogService.java
package com.zoo.weight;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface WeightLogService {
    WeightLogEntity create(Long animalId, WeightLogEntity body);
    Page<WeightLogEntity> list(Long animalId,
                               LocalDateTime from, LocalDateTime to,
                               BigDecimal minKg, BigDecimal maxKg,
                               Pageable pageable);
    Optional<WeightLogEntity> get(Long id);
    Optional<WeightLogEntity> update(Long id, WeightLogEntity body);
    void delete(Long id);
}
