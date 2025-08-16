package com.zoo.staff;

import com.zoo.animal.AnimalEntity;
import com.zoo.animal.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StaffService {

    private final StaffRepository staffRepository;
    private final AnimalRepository animalRepository;

    public StaffService(StaffRepository staffRepository, AnimalRepository animalRepository) {
        this.staffRepository = staffRepository;
        this.animalRepository = animalRepository;
    }

    private StaffDTO toDTO(StaffEntity e) {
        List<Long> animalIds = e.getAnimals().stream().map(AnimalEntity::getId).collect(Collectors.toList());
        return new StaffDTO(e.getId(), e.getName(), e.getRole(), e.getEmail(), e.getPhone(), animalIds);
    }

    private void apply(StaffEntity e, StaffDTO dto) {
        e.setName(dto.getName());
        e.setRole(dto.getRole());
        e.setEmail(dto.getEmail());
        e.setPhone(dto.getPhone());
    }

    public StaffDTO create(StaffDTO dto) {
        StaffEntity e = new StaffEntity();
        apply(e, dto);
        return toDTO(staffRepository.save(e));
    }

    public List<StaffDTO> list() {
        return staffRepository.findAll().stream().map(this::toDTO).toList();
    }

    public Optional<StaffDTO> get(Long id) {
        return staffRepository.findById(id).map(this::toDTO);
    }

    public Optional<StaffDTO> update(Long id, StaffDTO dto) {
        return staffRepository.findById(id).map(e -> {
            apply(e, dto);
            return toDTO(staffRepository.save(e));
        });
    }

    public void delete(Long id) {
        staffRepository.deleteById(id);
    }

    // assignments
    public List<Long> getAssignedAnimalIds(Long staffId) {
        StaffEntity s = staffRepository.findById(staffId).orElseThrow();
        return s.getAnimals().stream().map(AnimalEntity::getId).toList();
    }

    public void assign(Long staffId, Long animalId) {
        StaffEntity s = staffRepository.findById(staffId).orElseThrow();
        AnimalEntity a = animalRepository.findById(animalId).orElseThrow();
        s.getAnimals().add(a);
        staffRepository.save(s);
    }

    public void unassign(Long staffId, Long animalId) {
        StaffEntity s = staffRepository.findById(staffId).orElseThrow();
        s.getAnimals().removeIf(a -> a.getId().equals(animalId));
        staffRepository.save(s);
    }
}
