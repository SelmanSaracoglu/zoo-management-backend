package com.zoo.health;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    // Create health check for an animal
    @PostMapping("/api/animals/{animalId}/health-checks")
    public ResponseEntity<HealthCheckEntity> create(
            @PathVariable Long animalId,
            @RequestBody HealthCheckEntity body
    ) {
        return ResponseEntity.ok(healthCheckService.createForAnimal(animalId, body));
    }

    // List health checks by animal (optional date range)
    @GetMapping("/api/animals/{animalId}/health-checks")
    public ResponseEntity<List<HealthCheckEntity>> list(
            @PathVariable Long animalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        if (from != null && to != null) {
            return ResponseEntity.ok(healthCheckService.listByAnimalAndRange(animalId, from, to));
        }
        return ResponseEntity.ok(healthCheckService.listByAnimal(animalId));
    }

    // Get one
    @GetMapping("/api/health-checks/{checkId}")
    public ResponseEntity<HealthCheckEntity> getOne(@PathVariable Long checkId) {
        return healthCheckService.findById(checkId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/api/health-checks/{checkId}")
    public ResponseEntity<HealthCheckEntity> update(
            @PathVariable Long checkId,
            @RequestBody HealthCheckEntity body
    ) {
        return healthCheckService.update(checkId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/api/health-checks/{checkId}")
    public ResponseEntity<Void> delete(@PathVariable Long checkId) {
        healthCheckService.delete(checkId);
        return ResponseEntity.noContent().build();
    }


}
