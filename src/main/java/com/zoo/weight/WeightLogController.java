package com.zoo.weight;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class WeightLogController {

    private final WeightLogService weightLogService;

    public WeightLogController(WeightLogService weightLogService) {
        this.weightLogService = weightLogService;
    }

    @PostMapping("/api/animals/{animalId}/weights")
    public ResponseEntity<WeightLogEntity> create(
            @PathVariable Long animalId,
            @RequestBody WeightLogEntity body
    ) {
        return ResponseEntity.ok(weightLogService.createForAnimal(animalId, body));
    }

    @GetMapping("/api/animals/{animalId}/weights")
    public ResponseEntity<List<WeightLogEntity>> list(
            @PathVariable Long animalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        if (from != null && to != null) {
            return ResponseEntity.ok(weightLogService.listByAnimalAndRange(animalId, from, to));
        }
        return ResponseEntity.ok(weightLogService.listByAnimal(animalId));
    }

    @GetMapping("/api/weights/{weightId}")
    public ResponseEntity<WeightLogEntity> getOne(@PathVariable Long weightId) {
        return weightLogService.findById(weightId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/api/weights/{weightId}")
    public ResponseEntity<WeightLogEntity> update(
            @PathVariable Long weightId,
            @RequestBody WeightLogEntity body
    ) {
        return weightLogService.update(weightId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/weights/{weightId}")
    public ResponseEntity<Void> delete(@PathVariable Long weightId) {
        weightLogService.delete(weightId);
        return ResponseEntity.noContent().build();
    }
}
