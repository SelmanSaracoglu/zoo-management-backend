package com.zoo.feed;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedLogRepository extends JpaRepository<FeedLogEntity, Long> {

    // Belirli hayvanın tüm feed kayıtları (zaman sıralı)
    List<FeedLogEntity> findByAnimal_IdOrderByFeedTimeDesc(Long animalId);

    Optional<FeedLogEntity> findFirstByAnimal_IdOrderByFeedTimeDesc(Long animalId);

    boolean existsByAnimal_IdAndFeedTime(Long animalId, LocalDateTime feedTime);
    boolean existsByAnimal_IdAndFeedTimeAndIdNot(Long animalId, LocalDateTime feedTime, Long id);

}
