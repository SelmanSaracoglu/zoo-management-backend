package com.zoo.staff;

import com.zoo.animal.AnimalDTO; // eğer animals listesine döneceksek kullanırız, şimdilik id listesi yeter
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffService staffService;
    public StaffController(StaffService staffService) { this.staffService = staffService; }

    @GetMapping
    public ResponseEntity<List<StaffDTO>> list() { return ResponseEntity.ok(staffService.list()); }

    @PostMapping
    public ResponseEntity<StaffDTO> create(@RequestBody StaffDTO dto) {
        return ResponseEntity.ok(staffService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDTO> get(@PathVariable Long id) {
        return staffService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDTO> update(@PathVariable Long id, @RequestBody StaffDTO dto) {
        return staffService.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // FE: /staff/{staffId}/animals  (id listesi)
    @GetMapping("/{staffId}/animals")
    public ResponseEntity<List<Long>> getAssignedAnimalIds(@PathVariable Long staffId) {
        return ResponseEntity.ok(staffService.getAssignedAnimalIds(staffId));
    }

    // FE: /staff/{staffId}/assign/{animalId}
    @PostMapping("/{staffId}/assign/{animalId}")
    public ResponseEntity<Void> assign(@PathVariable Long staffId, @PathVariable Long animalId) {
        staffService.assign(staffId, animalId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{staffId}/assign/{animalId}")
    public ResponseEntity<Void> unassign(@PathVariable Long staffId, @PathVariable Long animalId) {
        staffService.unassign(staffId, animalId);
        return ResponseEntity.noContent().build();
    }
}
