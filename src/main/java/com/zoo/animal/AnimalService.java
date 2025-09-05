// src/main/java/com/zoo/animal/AnimalService.java
package com.zoo.animal;

import com.zoo.animal.AnimalDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AnimalService { AnimalEntity create(AnimalDto dto);
    Page<AnimalEntity> list(String species, String nameLike, String originCountry,
                            Gender gender, Boolean canSwim, Boolean canFly,
                            Integer minAge, Integer maxAge,
                            Pageable pageable);
    Optional<AnimalEntity> get(Long id);
    Optional<AnimalEntity> update(Long id, AnimalDto dto);
    void delete(Long id);
}
