package com.zoo.medication;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    // Create medication for an animal
    @PostMapping("/api/animals/{animalId}/medications")
    public ResponseEntity<MedicationEntity> create(
            @PathVariable Long animalId,
            @RequestBody MedicationEntity body
    ) {
        return ResponseEntity.ok(medicationService.createForAnimal(animalId, body));
    }

    // List medications by animal (optional date range)
    @GetMapping("/api/animals/{animalId}/medications")
    public ResponseEntity<List<MedicationEntity>> list(
            @PathVariable Long animalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        if (from != null && to != null) {
            return ResponseEntity.ok(medicationService.listByAnimalAndRange(animalId, from, to));
        }
        return ResponseEntity.ok(medicationService.listByAnimal(animalId));
    }

    // Get one
    @GetMapping("/api/medications/{medId}")
    public ResponseEntity<MedicationEntity> getOne(@PathVariable Long medId) {
        return medicationService.findById(medId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/api/medications/{medId}")
    public ResponseEntity<MedicationEntity> update(
            @PathVariable Long medId,
            @RequestBody MedicationEntity body
    ) {
        return medicationService.update(medId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/api/medications/{medId}")
    public ResponseEntity<Void> delete(@PathVariable Long medId) {
        medicationService.delete(medId);
        return ResponseEntity.noContent().build();
    }
}
