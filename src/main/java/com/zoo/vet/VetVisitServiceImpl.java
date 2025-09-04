// src/main/java/com/zoo/vet/VetVisitServiceImpl.java
package com.zoo.vet;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.BadRequestException;
import com.zoo.error.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class VetVisitServiceImpl implements VetVisitService {

    private final VetVisitRepository repo;
    private final AnimalRepository animalRepo;

    public VetVisitServiceImpl(VetVisitRepository repo, AnimalRepository animalRepo) {
        this.repo = repo; this.animalRepo = animalRepo;
    }

    @Override
    public VetVisitEntity create(Long animalId, VetVisitEntity body) {
        AnimalEntity animal = animalRepo.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal not found: " + animalId));

        if (body.getVisitTime() == null)
            throw new BadRequestException("visitTime must not be null");

        VetVisitEntity e = new VetVisitEntity();
        e.setAnimal(animal);
        e.setVisitTime(body.getVisitTime());
        e.setPurpose(body.getPurpose());
        e.setDiagnosis(body.getDiagnosis());
        e.setTreatment(body.getTreatment());
        e.setFollowUpOn(body.getFollowUpOn());
        return repo.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<VetVisitEntity> list(Long animalId,
                                     String purposeLike, String diagnosisLike,
                                     LocalDateTime from, LocalDateTime to,
                                     LocalDate followUpFrom, LocalDate followUpTo,
                                     Pageable pageable) {
        if (from != null && to != null && to.isBefore(from)) {
            throw new BadRequestException("to must be on/after from");
        }
        if (followUpFrom != null && followUpTo != null && followUpTo.isBefore(followUpFrom)) {
            throw new BadRequestException("followUpTo must be on/after followUpFrom");
        }

        List<Specification<VetVisitEntity>> specs = new ArrayList<>();
        specs.add((r,q,cb) -> cb.equal(r.get("animal").get("id"), animalId));

        if (purposeLike != null && !purposeLike.isBlank()) {
            String like = "%" + purposeLike.toLowerCase() + "%";
            specs.add((r,q,cb) -> cb.like(cb.lower(r.get("purpose")), like));
        }
        if (diagnosisLike != null && !diagnosisLike.isBlank()) {
            String like = "%" + diagnosisLike.toLowerCase() + "%";
            specs.add((r,q,cb) -> cb.like(cb.lower(r.get("diagnosis")), like));
        }
        if (from != null) specs.add((r,q,cb) -> cb.greaterThanOrEqualTo(r.get("visitTime"), from));
        if (to   != null) specs.add((r,q,cb) -> cb.lessThanOrEqualTo(r.get("visitTime"), to));
        if (followUpFrom != null) specs.add((r,q,cb) -> cb.greaterThanOrEqualTo(r.get("followUpOn"), followUpFrom));
        if (followUpTo   != null) specs.add((r,q,cb) -> cb.lessThanOrEqualTo(r.get("followUpOn"), followUpTo));

        Specification<VetVisitEntity> spec = Specification.allOf(specs);
        return repo.findAll(spec, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<VetVisitEntity> get(Long id) {
        return repo.findById(id);
    }

    @Override
    public Optional<VetVisitEntity> update(Long id, VetVisitEntity body) {
        return repo.findById(id).map(e -> {
            if (body.getVisitTime() == null)
                throw new BadRequestException("visitTime must not be null");

            e.setVisitTime(body.getVisitTime());
            e.setPurpose(body.getPurpose());
            e.setDiagnosis(body.getDiagnosis());
            e.setTreatment(body.getTreatment());
            e.setFollowUpOn(body.getFollowUpOn());
            return repo.save(e);
        }).or(() -> { throw new NotFoundException("VetVisit not found: " + id); });
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
