package com.zoo.medication;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // Create medication for an animal
    @PostMapping("/animals/{animalId}/medications")
    public ResponseEntity<MedicationEntity> create(
            @PathVariable Long animalId,
            @RequestBody MedicationEntity body
    ) {
        return ResponseEntity.ok(medicationService.createForAnimal(animalId, body));
    }

    // List medications by animal (optional date range)
    @GetMapping("/animals/{animalId}/medications")
    public ResponseEntity<List<MedicationEntity>> list( @PathVariable Long animalId    )
    { return ResponseEntity.ok(medicationService.listByAnimal(animalId)); }

    // Latest by animal (opsiyonel)
    @GetMapping("/animals/{animalId}/medications/latest")
    public ResponseEntity<MedicationEntity> latest(@PathVariable Long animalId) {
        return medicationService.latestByAnimal(animalId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build()); // 204 if none
    }

    // Get one
    @GetMapping("/medications/{medId}")
    public ResponseEntity<MedicationEntity> getOne(@PathVariable Long medId) {
        return medicationService.findById(medId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/medications/{medId}")
    public ResponseEntity<MedicationEntity> update(
            @PathVariable Long medId,
            @RequestBody MedicationEntity body
    ) {
        return medicationService.update(medId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/medications/{medId}")
    public ResponseEntity<Void> delete(@PathVariable Long medId) {
        medicationService.delete(medId);
        return ResponseEntity.noContent().build();
    }
}
