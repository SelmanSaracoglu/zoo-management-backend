package com.zoo.weight;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WeightLogController {

    private final WeightLogService weightLogService;

    public WeightLogController(WeightLogService weightLogService) {
        this.weightLogService = weightLogService;
    }

    @PostMapping("/animals/{animalId}/weights")
    public ResponseEntity<WeightLogEntity> create(
            @PathVariable Long animalId,
            @RequestBody WeightLogEntity body
    ) {
        return ResponseEntity.ok(weightLogService.createForAnimal(animalId, body));
    }

    @GetMapping("/animals/{animalId}/weights")
    public ResponseEntity<List<WeightLogEntity>> list(
            @PathVariable Long animalId) {
        return ResponseEntity.ok(weightLogService.listByAnimal(animalId));
    }

    @GetMapping("/api/weights/{weightId}")
    public ResponseEntity<WeightLogEntity> getOne(@PathVariable Long weightId) {
        return weightLogService.findById(weightId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/animals/{animalId}/weights/latest")
    public ResponseEntity<WeightLogEntity> latest(@PathVariable Long animalId) {
        return weightLogService.latestByAnimal(animalId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
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
