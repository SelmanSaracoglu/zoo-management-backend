package com.zoo.staff;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffService {

    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    public StaffEntity create(StaffEntity body) {
        // ID asla elle set etmiyoruz; yeni entity oluşturup alanları kopyalıyoruz
        StaffEntity e = new StaffEntity();
        e.setFullName(body.getFullName());
        e.setRole(body.getRole());
        return staffRepository.save(e);
    }

    public List<StaffEntity> list() {
        return staffRepository.findAll();
    }

    public Optional<StaffEntity> get(Long id) {
        return staffRepository.findById(id);
    }

    public Optional<StaffEntity> update(Long id, StaffEntity body) {
        return staffRepository.findById(id).map(existing -> {
            existing.setFullName(body.getFullName());
            existing.setRole(body.getRole());
            return staffRepository.save(existing);
        });
    }

    public void delete(Long id) {
        staffRepository.deleteById(id);
    }
}
