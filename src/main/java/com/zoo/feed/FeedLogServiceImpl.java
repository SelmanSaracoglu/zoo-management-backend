// src/main/java/com/zoo/feed/FeedLogServiceImpl.java
package com.zoo.feed;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.BadRequestException;
import com.zoo.error.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FeedLogServiceImpl implements FeedLogService {

    private final FeedLogRepository repo;
    private final AnimalRepository animalRepo;

    public FeedLogServiceImpl(FeedLogRepository repo, AnimalRepository animalRepo) {
        this.repo = repo; this.animalRepo = animalRepo;
    }

    @Override
    public FeedLogEntity create(Long animalId, FeedLogEntity body) {
        AnimalEntity animal = animalRepo.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

        // 400 validations
        if (body.getFeedTime() == null) throw new BadRequestException("feedTime must not be null");
        if (body.getFoodItem() == null || body.getFoodItem().isBlank()) throw new BadRequestException("foodItem must not be blank");
        if (body.getQuantityGrams() != null && body.getQuantityGrams() <= 0) throw new BadRequestException("quantityGrams must be > 0");
        if (body.getWaterMilliliters() != null && body.getWaterMilliliters() <= 0) throw new BadRequestException("waterMilliliters must be > 0");

        FeedLogEntity e = new FeedLogEntity();
        e.setAnimal(animal);
        e.setFeedTime(body.getFeedTime());
        e.setFoodItem(body.getFoodItem());
        e.setQuantityGrams(body.getQuantityGrams());
        e.setWaterMilliliters(body.getWaterMilliliters());
        e.setNotes(body.getNotes());
        return repo.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<FeedLogEntity> list(Long animalId, String foodLike, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new BadRequestException("to must be on/after from");
        }
        List<Specification<FeedLogEntity>> specs = new ArrayList<>();

        // base filtre (zorunlu)
        specs.add((root, q, cb) -> cb.equal(root.get("animal").get("id"), animalId));

        if (foodLike != null && !foodLike.isBlank()) {
            String like = "%" + foodLike.toLowerCase() + "%";
            specs.add((root, q, cb) -> cb.like(cb.lower(root.get("foodItem")), like));
        }
        if (from != null) {
            specs.add((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("feedTime"), from));
        }
        if (to != null) {
            specs.add((root, q, cb) -> cb.lessThanOrEqualTo(root.get("feedTime"), to));
        }
        // 3.5+ önerilen kullanım:
        Specification<FeedLogEntity> spec = Specification.allOf(specs);

        return repo.findAll(spec, pageable);
    }

    @Override @Transactional(readOnly = true)
    public java.util.Optional<FeedLogEntity> get(Long id) {
        return repo.findById(id);
    }

    @Override
    public java.util.Optional<FeedLogEntity> update(Long id, FeedLogEntity body) {
        return repo.findById(id).map(e -> {
            // 400 validations
            if (body.getFeedTime() == null) throw new BadRequestException("feedTime must not be null");
            if (body.getFoodItem() == null || body.getFoodItem().isBlank()) throw new BadRequestException("foodItem must not be blank");
            if (body.getQuantityGrams() != null && body.getQuantityGrams() <= 0) throw new BadRequestException("quantityGrams must be > 0");
            if (body.getWaterMilliliters() != null && body.getWaterMilliliters() <= 0) throw new BadRequestException("waterMilliliters must be > 0");

            e.setFeedTime(body.getFeedTime());
            e.setFoodItem(body.getFoodItem());
            e.setQuantityGrams(body.getQuantityGrams());
            e.setWaterMilliliters(body.getWaterMilliliters());
            e.setNotes(body.getNotes());
            return repo.save(e);
        }).or(() -> { throw new NotFoundException("FeedLog not found: " + id); });
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
