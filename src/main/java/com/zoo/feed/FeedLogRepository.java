package com.zoo.feed;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedLogRepository extends JpaRepository<FeedLogEntity, Long>, JpaSpecificationExecutor<FeedLogEntity> {
    Page<FeedLogEntity> findByAnimal_Id(Long animalId, Pageable pageable);
}
