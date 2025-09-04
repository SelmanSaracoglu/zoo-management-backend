package com.zoo.feed;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface FeedLogService {
    FeedLogEntity create(Long animalId, FeedLogEntity body);
    Page<FeedLogEntity> list(Long animalId, String foodLike, LocalDateTime from, LocalDateTime to, Pageable pageable);
    Optional<FeedLogEntity> get(Long id);
    Optional<FeedLogEntity> update(Long id, FeedLogEntity body);
    void delete(Long id);
}
