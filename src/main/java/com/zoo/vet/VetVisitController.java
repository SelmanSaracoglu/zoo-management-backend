package com.zoo.vet;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class VetVisitController {

    private final VetVisitService vetVisitService;

    public VetVisitController(VetVisitService vetVisitService) {
        this.vetVisitService = vetVisitService;
    }

    // Create vet visit for an animal
    @PostMapping("api/animals/{animalId}/vet-visits")
    public ResponseEntity<VetVisitEntity> create (
            @PathVariable Long animalId,
            @RequestBody VetVisitEntity body) {
        return ResponseEntity.ok(vetVisitService.createForAnimal(animalId, body));
    }

    // List visits by animal (optional date range)
    @GetMapping("/api/animals/{animalId}/vet-visits")
    public ResponseEntity<List<VetVisitEntity>> list(
            @PathVariable Long animalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        if (from != null && to != null) {
            return ResponseEntity.ok(vetVisitService.listByAnimalAndRange(animalId, from, to));
        }
        return ResponseEntity.ok(vetVisitService.listByAnimal(animalId));
    }

    // Get one
    @GetMapping("/api/vet-visits/{visitId}")
    public ResponseEntity<VetVisitEntity> getOne(@PathVariable Long visitId) {
        return vetVisitService.findById(visitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/api/vet-visits/{visitId}")
    public ResponseEntity<VetVisitEntity> update(
            @PathVariable Long visitId,
            @RequestBody VetVisitEntity body
    ) {
        return vetVisitService.update(visitId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/api/vet-visits/{visitId}")
    public ResponseEntity<Void> delete(@PathVariable Long visitId) {
        vetVisitService.delete(visitId);
        return ResponseEntity.noContent().build();
    }
}
