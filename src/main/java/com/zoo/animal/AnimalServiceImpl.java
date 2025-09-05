// src/main/java/com/zoo/animal/AnimalServiceImpl.java
package com.zoo.animal;

import com.zoo.animal.AnimalDto;
import com.zoo.animal.AnimalMapper;
import com.zoo.error.BadRequestException;
import com.zoo.error.ConflictException;
import com.zoo.error.NotFoundException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository repo;
    public AnimalServiceImpl(AnimalRepository repo){ this.repo = repo; }

    private void validate(AnimalDto d, boolean updating, Long id){
        if (d.getAge()!=null && (d.getAge()<0 || d.getAge()>300))
            throw new BadRequestException("age must be between 0-300");
        if (d.getName()!=null && !d.getName().isBlank()) {
            boolean exists = repo.existsByNameIgnoreCase(d.getName());
            if (exists && !updating) throw new ConflictException("name already exists");
            if (exists && updating) {
                repo.findById(id).ifPresent(e -> {
                    if (!d.getName().equalsIgnoreCase(e.getName())) {
                        throw new ConflictException("name already exists");
                    }
                });
            }
        }
    }

    @Override
    public AnimalEntity create(AnimalDto dto) {
        validate(dto, false, null);
        AnimalEntity e = new AnimalEntity();
        AnimalMapper.copyToEntity(dto, e);
        return repo.save(e);
    }

    @Override @Transactional(readOnly = true)
    public Page<AnimalEntity> list(String species, String nameLike, String originCountry,
                                   Gender gender, Boolean canSwim, Boolean canFly,
                                   Integer minAge, Integer maxAge,
                                   Pageable pageable) {
        if (minAge!=null && minAge<0) throw new BadRequestException("minAge must be >= 0");
        if (maxAge!=null && maxAge<0) throw new BadRequestException("maxAge must be >= 0");
        if (minAge!=null && maxAge!=null && maxAge<minAge) throw new BadRequestException("maxAge must be >= minAge");

        List<Specification<AnimalEntity>> specs = new ArrayList<>();
        if (species!=null && !species.isBlank())
            specs.add((r,q,cb)-> cb.equal(cb.lower(r.get("species")), species.toLowerCase()));
        if (nameLike!=null && !nameLike.isBlank()){
            String like = "%"+nameLike.toLowerCase()+"%";
            specs.add((r,q,cb)-> cb.like(cb.lower(r.get("name")), like));
        }
        if (originCountry!=null && !originCountry.isBlank())
            specs.add((r,q,cb)-> cb.equal(cb.lower(r.get("originCountry")), originCountry.toLowerCase()));
        if (gender!=null)
            specs.add((r,q,cb)-> cb.equal(r.get("gender"), gender));
        if (canSwim!=null)
            specs.add((r,q,cb)-> (canSwim ? cb.isTrue(r.get("canSwim")) : cb.isFalse(r.get("canSwim"))));
        if (canFly!=null)
            specs.add((r,q,cb)-> (canFly ? cb.isTrue(r.get("canFly")) : cb.isFalse(r.get("canFly"))));
        if (minAge!=null)
            specs.add((r,q,cb)-> cb.greaterThanOrEqualTo(r.get("age"), minAge));
        if (maxAge!=null)
            specs.add((r,q,cb)-> cb.lessThanOrEqualTo(r.get("age"), maxAge));

        Specification<AnimalEntity> spec = Specification.allOf(specs);
        return repo.findAll(spec, pageable);
    }

    @Override @Transactional(readOnly = true)
    public Optional<AnimalEntity> get(Long id) { return repo.findById(id); }

    @Override
    public Optional<AnimalEntity> update(Long id, AnimalDto dto) {
        validate(dto, true, id);
        return repo.findById(id).map(e -> {
            AnimalMapper.copyToEntity(dto, e);
            return repo.save(e);
        }).or(() -> { throw new NotFoundException("Animal not found: " + id); });
    }

    @Override
    public void delete(Long id) { repo.deleteById(id); }
}
