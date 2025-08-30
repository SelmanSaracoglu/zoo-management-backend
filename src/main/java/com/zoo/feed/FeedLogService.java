package com.zoo.feed;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FeedLogService {

    private final FeedLogRepository feedLogRepository;
    private final AnimalRepository animalRepository;

    public FeedLogService(FeedLogRepository feedLogRepository,
                          AnimalRepository animalRepository) {
        this.feedLogRepository = feedLogRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public FeedLogEntity createForAnimal(Long animalId, FeedLogEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(()-> new IllegalArgumentException("Animal not found: " + animalId));

        if (body.getFoodItem() == null || body.getFoodItem().isBlank()) {
            throw new ConflictException("foodItem must not be blank");
        }
        if (body.getFeedTime() == null) {
            throw new ConflictException("feedTime must not be null");
        }
        if (body.getQuantityGrams() != null && body.getQuantityGrams() <= 0) {
            throw new ConflictException("quantityGrams must be > 0 when provided");
        }
        if (body.getWaterMilliliters() != null && body.getWaterMilliliters() < 0) {
            throw new ConflictException("waterMilliliters must be >= 0 when provided");
        }
        if (feedLogRepository.existsByAnimal_IdAndFeedTime(animalId, body.getFeedTime())) {
            throw new ConflictException("Duplicate feed at " + body.getFeedTime());
        }

        FeedLogEntity entity = new FeedLogEntity();
        entity.setAnimal(animal);                    // FK bağla
        entity.setFeedTime(body.getFeedTime());
        entity.setFoodItem(body.getFoodItem());
        entity.setQuantityGrams(body.getQuantityGrams());
        entity.setWaterMilliliters(body.getWaterMilliliters());
        entity.setNotes(body.getNotes());

        return feedLogRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<FeedLogEntity> listByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return feedLogRepository.findByAnimal_IdOrderByFeedTimeDesc(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<FeedLogEntity> latestByAnimal(Long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NotFoundException("Animal not found: " + animalId);
        }
        return feedLogRepository.findFirstByAnimal_IdOrderByFeedTimeDesc(animalId);
    }

    @Transactional(readOnly = true)
    public Optional<FeedLogEntity> findById(Long feedId) {
        return feedLogRepository.findById(feedId);
    }

    @Transactional
    public Optional<FeedLogEntity> update(Long feedId, FeedLogEntity body) {
        return feedLogRepository.findById(feedId).map(existing -> {
            Long animalId = existing.getAnimal().getId();

            if (body.getFoodItem() == null || body.getFoodItem().isBlank()) {
                throw new ConflictException("foodItem must not be blank");
            }
            if (body.getFeedTime() == null) {
                throw new ConflictException("feedTime must not be null");
            }
            if (body.getQuantityGrams() != null && body.getQuantityGrams() <= 0) {
                throw new ConflictException("quantityGrams must be > 0 when provided");
            }
            if (body.getWaterMilliliters() != null && body.getWaterMilliliters() < 0) {
                throw new ConflictException("waterMilliliters must be >= 0 when provided");
            }
            if (feedLogRepository.existsByAnimal_IdAndFeedTimeAndIdNot(
                    animalId, body.getFeedTime(), feedId)) {
                throw new ConflictException("Duplicate feed at " + body.getFeedTime());
            }

            existing.setFoodItem(body.getFoodItem());
            existing.setQuantityGrams(body.getQuantityGrams());
            existing.setWaterMilliliters(body.getWaterMilliliters());
            existing.setFeedTime(body.getFeedTime());
            existing.setNotes(body.getNotes());
            return feedLogRepository.save(existing);
        }).or(() -> {
            throw new NotFoundException("Feed log not found: " + feedId);
        });
    }

    @Transactional
    public void delete(Long feedId) {
        feedLogRepository.deleteById(feedId);
    }
}
