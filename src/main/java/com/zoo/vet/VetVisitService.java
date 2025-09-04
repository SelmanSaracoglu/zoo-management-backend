
package com.zoo.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface VetVisitService {
    VetVisitEntity create(Long animalId, VetVisitEntity body);
    Page<VetVisitEntity> list(Long animalId,
                              String purposeLike, String diagnosisLike,
                              LocalDateTime from, LocalDateTime to,
                              LocalDate followUpFrom, LocalDate followUpTo,
                              Pageable pageable);
    Optional<VetVisitEntity> get(Long id);
    Optional<VetVisitEntity> update(Long id, VetVisitEntity body);
    void delete(Long id);
}
