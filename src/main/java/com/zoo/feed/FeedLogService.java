package com.zoo.feed;


import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

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

    public FeedLogEntity createForAnimal(Long animalId, FeedLogEntity body) {
        AnimalEntity animal = animalRepository.findById(animalId)
                .orElseThrow(()-> new IllegalArgumentException("Animal not found: " + animalId));

        FeedLogEntity entity = new FeedLogEntity();
        entity.setAnimal(animal);                    // FK bağla
        entity.setFeedTime(body.getFeedTime());
        entity.setFoodItem(body.getFoodItem());
        entity.setQuantityGrams(body.getQuantityGrams());
        entity.setWaterMilliliters(body.getWaterMilliliters());
        entity.setNotes(body.getNotes());

        return feedLogRepository.save(entity);
    }
    public List<FeedLogEntity> listByAnimal(Long animalId) {
        return feedLogRepository.findByAnimal_IdOrderByFeedTimeDesc(animalId);
    }

    public List<FeedLogEntity> listByAnimalAndRange(Long animalId, LocalDateTime from, LocalDateTime to) {
        return feedLogRepository.findByAnimal_IdAndFeedTimeBetweenOrderByFeedTimeDesc(animalId, from, to);
    }

    public Optional<FeedLogEntity> findById(Long feedId) {
        return feedLogRepository.findById(feedId);
    }

    public Optional<FeedLogEntity> update(Long feedId, FeedLogEntity body) {
        return feedLogRepository.findById(feedId).map(existing -> {
            // animal değiştirmiyoruz; stabil kalsın
            existing.setFeedTime(body.getFeedTime());
            existing.setFoodItem(body.getFoodItem());
            existing.setQuantityGrams(body.getQuantityGrams());
            existing.setWaterMilliliters(body.getWaterMilliliters());
            existing.setNotes(body.getNotes());
            return feedLogRepository.save(existing);
        });
    }

    public void delete(Long feedId) {
        feedLogRepository.deleteById(feedId);
    }
}
