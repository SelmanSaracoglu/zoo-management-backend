// src/main/java/com/zoo/staff/StaffService.java
package com.zoo.staff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StaffService {
    StaffEntity create(com.zoo.staff.StaffDto dto);
    Page<StaffEntity> list(String role, String nameLike, String emailLike, Long animalId, Pageable pageable);
    Optional<StaffEntity> get(Long id);
    Optional<StaffEntity> update(Long id, com.zoo.staff.StaffDto dto);
    void delete(Long id);
}
