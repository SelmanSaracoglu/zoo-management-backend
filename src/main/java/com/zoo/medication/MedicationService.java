
package com.zoo.medication;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MedicationService {
    MedicationEntity create(Long animalId, MedicationEntity body);
    Page<MedicationEntity> list(Long animalId, String medLike, String route,
                                LocalDateTime from, LocalDateTime to,
                                Pageable pageable);
    Optional<MedicationEntity> get(Long id);
    Optional<MedicationEntity> update(Long id, MedicationEntity body);
    void delete(Long id);
}
