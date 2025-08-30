package com.zoo.vet;

import com.zoo.animal.AnimalRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class VetVisitController {

    private final VetVisitService vetVisitService;

    public VetVisitController(VetVisitService vetVisitService) {
        this.vetVisitService = vetVisitService;
    }

    // Create vet visit for an animal
    @PostMapping("/animals/{animalId}/vet-visits")
    public ResponseEntity<VetVisitEntity> create (
            @PathVariable Long animalId,
            @RequestBody VetVisitEntity body) {
        return ResponseEntity.ok(vetVisitService.createForAnimal(animalId, body));
    }

    @GetMapping("/animals/{animalId}/vet-visits/latest")
    public ResponseEntity<VetVisitEntity> latest(@PathVariable Long animalId) {
        Optional<VetVisitEntity> opt = vetVisitService.latestByAnimal(animalId);
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build()); // 204 if no visits
    }

    // List visits by animal (optional date range)
    @GetMapping("/animals/{animalId}/vet-visits")
    public ResponseEntity<List<VetVisitEntity>> listAll(@PathVariable Long animalId) {
        return ResponseEntity.ok(vetVisitService.listByAnimal(animalId));
    }

    // Get one
    @GetMapping("/vet-visits/{visitId}")
    public ResponseEntity<VetVisitEntity> getOne(@PathVariable Long visitId) {
        return vetVisitService.findById(visitId)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.zoo.error.NotFoundException("Vet visit not found: " + visitId));
    }

    // Update
    @PutMapping("/vet-visits/{visitId}")
    public ResponseEntity<VetVisitEntity> update(
            @PathVariable Long visitId,
            @RequestBody VetVisitEntity body
    ) {
        return vetVisitService.update(visitId, body)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new com.zoo.error.NotFoundException("Vet visit not found: " + visitId));
    }

    // Delete
    @DeleteMapping("/vet-visits/{visitId}")
    public ResponseEntity<Void> delete(@PathVariable Long visitId) {
        vetVisitService.delete(visitId);
        return ResponseEntity.noContent().build();
    }
}
