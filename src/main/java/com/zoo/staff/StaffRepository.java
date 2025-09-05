// src/main/java/com/zoo/staff/StaffRepository.java
package com.zoo.staff;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, Long>, JpaSpecificationExecutor<StaffEntity> {
    boolean existsByEmailIgnoreCase(String email);
}
