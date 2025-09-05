// src/main/java/com/zoo/weight/WeightLogServiceImpl.java
package com.zoo.weight;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.BadRequestException;
import com.zoo.error.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class WeightLogServiceImpl implements WeightLogService {

    private final WeightLogRepository repo;
    private final AnimalRepository animalRepo;

    public WeightLogServiceImpl(WeightLogRepository repo, AnimalRepository animalRepo) {
        this.repo = repo; this.animalRepo = animalRepo;
    }

    @Override
    public WeightLogEntity create(Long animalId, WeightLogEntity body) {
        AnimalEntity animal = animalRepo.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

        // 400 validations
        if (body.getMeasuredAt() == null) throw new BadRequestException("measuredAt must not be null");
        if (body.getWeightKg() == null || body.getWeightKg().signum() <= 0)
            throw new BadRequestException("weightKg must be > 0");

        WeightLogEntity e = new WeightLogEntity();
        e.setAnimal(animal);
        e.setMeasuredAt(body.getMeasuredAt());
        e.setWeightKg(body.getWeightKg());
        return repo.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<WeightLogEntity> list(Long animalId,
                                      LocalDateTime from, LocalDateTime to,
                                      BigDecimal minKg, BigDecimal maxKg,
                                      Pageable pageable) {
        if (from != null && to != null && to.isBefore(from))
            throw new BadRequestException("to must be on/after from");
        if (minKg != null && minKg.signum() <= 0)
            throw new BadRequestException("minKg must be > 0");
        if (maxKg != null && maxKg.signum() <= 0)
            throw new BadRequestException("maxKg must be > 0");
        if (minKg != null && maxKg != null && maxKg.compareTo(minKg) < 0)
            throw new BadRequestException("maxKg must be >= minKg");

        List<Specification<WeightLogEntity>> specs = new ArrayList<>();
        specs.add((r,q,cb) -> cb.equal(r.get("animal").get("id"), animalId));
        if (from != null) specs.add((r,q,cb) -> cb.greaterThanOrEqualTo(r.get("measuredAt"), from));
        if (to   != null) specs.add((r,q,cb) -> cb.lessThanOrEqualTo(r.get("measuredAt"), to));
        if (minKg != null) specs.add((r,q,cb) -> cb.greaterThanOrEqualTo(r.get("weightKg"), minKg));
        if (maxKg != null) specs.add((r,q,cb) -> cb.lessThanOrEqualTo(r.get("weightKg"), maxKg));

        Specification<WeightLogEntity> spec = Specification.allOf(specs);
        return repo.findAll(spec, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<WeightLogEntity> get(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<WeightLogEntity> update(Long id, WeightLogEntity body) {
        return repo.findById(id).map(e -> {
            if (body.getMeasuredAt() == null) throw new BadRequestException("measuredAt must not be null");
            if (body.getWeightKg() == null || body.getWeightKg().signum() <= 0)
                throw new BadRequestException("weightKg must be > 0");

            e.setMeasuredAt(body.getMeasuredAt());
            e.setWeightKg(body.getWeightKg());
            return repo.save(e);
        }).or(() -> { throw new NotFoundException("WeightLog not found: " + id); });
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
