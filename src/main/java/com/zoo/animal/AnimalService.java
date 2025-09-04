package com.zoo.animal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository){
        this.animalRepository = animalRepository;
    }

    /* Entity -> DTO */
    private AnimalDTO toDTO(AnimalEntity e) {
        if (e == null) return null;
        return new AnimalDTO(
                e.getId(),
                e.getName(),
                e.getSpecies(),
                e.getHabitat(),
                e.getDiet(),
                e.getOriginCountry(),
                e.getAge(),                                              // Integer (nullable)
                e.getGender() == null ? Gender.UNKNOWN : e.getGender(), // Enum
                e.isCanSwim(),
                e.isCanFly()
        );
    }

    /* DTO -> Entity (create/update ortak) */
    private AnimalEntity applyFromDto(AnimalEntity t, AnimalDTO dto) {
        t.setName(dto.getName());
        t.setSpecies(dto.getSpecies());
        t.setHabitat(dto.getHabitat());
        t.setDiet(dto.getDiet());
        t.setOriginCountry(dto.getOriginCountry());

        // age: null veya <=0 ise DB'de NULL tut
        Integer a = dto.getAge();
        t.setAge(a == null || a.intValue() <= 0 ? null : a);

        // gender: null ise UNKNOWN
        t.setGender(dto.getGender() == null ? Gender.UNKNOWN : dto.getGender());

        t.setCanSwim(dto.isCanSwim());
        t.setCanFly(dto.isCanFly());
        return t;
    }

    // CREATE
    public AnimalDTO createAnimal(AnimalDTO dto){
        AnimalEntity animal = new AnimalEntity();
        applyFromDto(animal, dto);
        AnimalEntity saved = animalRepository.save(animal);
        return toDTO(saved);
    }

    // READ - All
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Page<AnimalDTO> getAnimalsPage(Pageable pageable) {
        return animalRepository.findAll(pageable)
                .map(this::toDTO);
    }

    // READ - One
    public Optional<AnimalDTO> getAnimalById(Long id) {
        return animalRepository.findById(id).map(this::toDTO);
    }

    // UPDATE
    public Optional<AnimalDTO> updateAnimal(Long id, AnimalDTO dto) {
        return animalRepository.findById(id).map(existing -> {
            applyFromDto(existing, dto);
            AnimalEntity saved = animalRepository.save(existing);
            return toDTO(saved);
        });
    }

    // DELETE
    public void deleteAnimal(Long id) {
        animalRepository.deleteById(id);
    }
}
