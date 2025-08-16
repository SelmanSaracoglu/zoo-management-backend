package com.zoo.feed;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FeedLogRepository extends JpaRepository<FeedLogEntity, Long> {

    // Belirli hayvanın tüm feed kayıtları (zaman sıralı)
    List<FeedLogEntity> findByAnimal_IdOrderByFeedTimeDesc(Long animalId);

    // Tarih aralığı filtreli (opsiyonel kullanım)
    List<FeedLogEntity> findByAnimal_IdAndFeedTimeBetweenOrderByFeedTimeDesc(
            Long animalId, LocalDateTime from, LocalDateTime to
    );

}
