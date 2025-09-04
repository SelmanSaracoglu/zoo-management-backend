// src/main/java/com/zoo/medication/MedicationServiceImpl.java
package com.zoo.medication;

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
import java.util.Optional;

@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository repo;
    private final AnimalRepository animalRepo;

    public MedicationServiceImpl(MedicationRepository repo, AnimalRepository animalRepo) {
        this.repo = repo; this.animalRepo = animalRepo;
    }

    @Override
    public MedicationEntity create(Long animalId, MedicationEntity body) {
        AnimalEntity animal = animalRepo.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

        // 400 — alan doğrulamaları
        if (body.getMedName() == null || body.getMedName().isBlank())
            throw new BadRequestException("medName must not be blank");
        if (body.getGivenAt() == null)
            throw new BadRequestException("givenAt must not be null");
        // dose/route/reason opsiyonel; route boş değilse max uzunluk zaten JPA’da

        MedicationEntity e = new MedicationEntity();
        e.setAnimal(animal);
        e.setMedName(body.getMedName());
        e.setDose(body.getDose());
        e.setRoute(body.getRoute());
        e.setGivenAt(body.getGivenAt());
        e.setReason(body.getReason());
        return repo.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<MedicationEntity> list(Long animalId, String medLike, String route,
                                       LocalDateTime from, LocalDateTime to,
                                       Pageable pageable) {

        if (from != null && to != null && to.isBefore(from)) {
            throw new BadRequestException("to must be on/after from");
        }

        List<Specification<MedicationEntity>> specs = new ArrayList<>();
        specs.add((root, q, cb) -> cb.equal(root.get("animal").get("id"), animalId));

        if (medLike != null && !medLike.isBlank()) {
            String like = "%" + medLike.toLowerCase() + "%";
            specs.add((root, q, cb) -> cb.like(cb.lower(root.get("medName")), like));
        }
        if (route != null && !route.isBlank()) {
            specs.add((root, q, cb) -> cb.equal(cb.lower(root.get("route")), route.toLowerCase()));
        }
        if (from != null) {
            specs.add((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("givenAt"), from));
        }
        if (to != null) {
            specs.add((root, q, cb) -> cb.lessThanOrEqualTo(root.get("givenAt"), to));
        }

        Specification<MedicationEntity> spec = Specification.allOf(specs);
        return repo.findAll(spec, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<MedicationEntity> get(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<MedicationEntity> update(Long id, MedicationEntity body) {
        return repo.findById(id).map(e -> {
            // 400 — doğrulamalar
            if (body.getMedName() == null || body.getMedName().isBlank())
                throw new BadRequestException("medName must not be blank");
            if (body.getGivenAt() == null)
                throw new BadRequestException("givenAt must not be null");

            e.setMedName(body.getMedName());
            e.setDose(body.getDose());
            e.setRoute(body.getRoute());
            e.setGivenAt(body.getGivenAt());
            e.setReason(body.getReason());
            return repo.save(e);
        }).or(() -> { throw new NotFoundException("Medication not found: " + id); });
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
