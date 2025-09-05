// src/main/java/com/zoo/staff/StaffServiceImpl.java
package com.zoo.staff;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import com.zoo.error.BadRequestException;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import com.zoo.staff.StaffDto;
import com.zoo.staff.StaffMapper;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StaffServiceImpl implements StaffService {

    private static final Set<String> ROLES = Set.of("KEEPER","VET","ADMIN");

    private final StaffRepository repo;
    private final AnimalRepository animalRepo;

    public StaffServiceImpl(StaffRepository repo, AnimalRepository animalRepo) {
        this.repo = repo; this.animalRepo = animalRepo;
    }

    private void validate(StaffDto dto, boolean isUpdate, Long currentId){
        if (dto.getRole()!=null && !ROLES.contains(dto.getRole().toUpperCase()))
            throw new BadRequestException("role must be one of: " + ROLES);
        if (dto.getEmail()!=null && !dto.getEmail().isBlank()) {
            // basit email uniqueness (opsiyonel)
            boolean exists = repo.existsByEmailIgnoreCase(dto.getEmail());
            if (exists) {
                // update ise aynı kaydın maili olabilir
                if (!(isUpdate && repo.findById(currentId).map(e -> dto.getEmail().equalsIgnoreCase(e.getEmail())).orElse(false))) {
                    throw new ConflictException("email already in use");
                }
            }
        }
    }

    private void replaceAnimals(StaffEntity e, Set<Long> ids){
        e.getAnimals().clear();
        if (ids == null || ids.isEmpty()) return;
        List<AnimalEntity> animals = animalRepo.findAllById(ids);
        if (animals.size() != ids.size())
            throw new NotFoundException("one or more animalIds do not exist");
        animals.forEach(e.getAnimals()::add);
    }

    @Override
    public StaffEntity create(StaffDto dto) {
        validate(dto, false, null);
        StaffEntity e = new StaffEntity();
        StaffMapper.copyBasics(dto, e);
        replaceAnimals(e, dto.getAnimalIds());
        return repo.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<StaffEntity> list(String role, String nameLike, String emailLike, Long animalId, Pageable pageable) {
        List<Specification<StaffEntity>> specs = new ArrayList<>();
        if (role != null && !role.isBlank())
            specs.add((r,q,cb) -> cb.equal(cb.lower(r.get("role")), role.toLowerCase()));
        if (nameLike != null && !nameLike.isBlank()) {
            String like = "%" + nameLike.toLowerCase() + "%";
            specs.add((r,q,cb) -> cb.like(cb.lower(r.get("name")), like));
        }
        if (emailLike != null && !emailLike.isBlank()) {
            String like = "%" + emailLike.toLowerCase() + "%";
            specs.add((r,q,cb) -> cb.like(cb.lower(r.get("email")), like));
        }
        if (animalId != null)
            specs.add((r,q,cb) -> cb.equal(r.join("animals").get("id"), animalId)); // many-to-many join

        Specification<StaffEntity> spec = Specification.allOf(specs);
        return repo.findAll(spec, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<StaffEntity> get(Long id) { return repo.findById(id); }

    @Override
    public Optional<StaffEntity> update(Long id, StaffDto dto) {
        validate(dto, true, id);
        return repo.findById(id).map(e -> {
            StaffMapper.copyBasics(dto, e);
            replaceAnimals(e, dto.getAnimalIds());
            return repo.save(e);
        }).or(() -> { throw new NotFoundException("Staff not found: " + id); });
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }
}
