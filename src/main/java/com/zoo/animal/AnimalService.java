package com.zoo.animal;

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

    // Entity → DTO
    private AnimalDTO convertToDTO(AnimalEntity animal) {
        return new AnimalDTO(
                animal.getId(),
                animal.getName(),
                animal.getSpecies(),
                animal.getHabitat(),
                animal.getDiet(),
                animal.getOriginCountry(),
                animal.getAge(),
                animal.getGender(),
                animal.isCanSwim(),
                animal.isCanFly()
        );
    }

    // DTO → Entity
    private AnimalEntity convertToEntity(AnimalDTO dto){
        AnimalEntity animal = new AnimalEntity();
        animal.setName(dto.getName());
        animal.setSpecies(dto.getSpecies());
        animal.setHabitat(dto.getHabitat());
        animal.setDiet(dto.getDiet());
        animal.setOriginCountry(dto.getOriginCountry());
        animal.setAge(dto.getAge());
        animal.setGender(dto.getGender());
        animal.setCanFly(dto.isCanFly());
        animal.setCanSwim(dto.isCanSwim());
        return animal;
    }

    // CREATE
    public AnimalDTO createAnimal(AnimalDTO dto){
        AnimalEntity animal = convertToEntity(dto);
        AnimalEntity saved = animalRepository.save(animal);
        return convertToDTO(saved);
    }

    // READ - All
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // READ - One
    public Optional<AnimalDTO> getAnimalById(Long id) {
        return animalRepository.findById(id)
                .map(this::convertToDTO);
    }

    // UPDATE
    public Optional<AnimalDTO> updateAnimal(Long id, AnimalDTO dto) {
        return animalRepository.findById(id).map(existing -> {
            existing.setName(dto.getName());
            existing.setSpecies(dto.getSpecies());
            existing.setHabitat(dto.getHabitat());
            existing.setDiet(dto.getDiet());
            existing.setOriginCountry(dto.getOriginCountry());
            existing.setAge(dto.getAge());
            existing.setGender(dto.getGender());
            existing.setCanFly(dto.isCanFly());
            existing.setCanSwim(dto.isCanSwim());

            AnimalEntity saved = animalRepository.save(existing);
            return convertToDTO(saved);
        });
    }

    // DELETE
    public void deleteAnimal(Long id) {
        animalRepository.deleteById(id);
    }

}
