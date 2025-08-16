package com.zoo.diet;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class DietPlanController {
    private final DietPlanService dietPlanService;

    public DietPlanController (DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    // Create plan for an animal (DTO'suz)
    @PostMapping("/api/animals/{animalId}/diet-plans")
    public ResponseEntity<DietPlanEntity> createForAnimal(
            @PathVariable Long animalId,
            @RequestBody DietPlanEntity body) {

        DietPlanEntity created = dietPlanService.createForAnimal(animalId, body);
        return ResponseEntity.ok(created);
    }

    // List plans by animal
    @GetMapping("/api/animals/{animalId}/diet-plans")
    public ResponseEntity<List<DietPlanEntity>> listByAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(dietPlanService.findByAnimal(animalId));
    }

    // Get one
    @GetMapping("/api/diet-plans/{planId}")
    public ResponseEntity<DietPlanEntity> getOne(@PathVariable Long planId) {
        return dietPlanService.findById(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update (ID sabit, animal sabit)
    @PutMapping("/api/diet-plans/{planId}")
    public ResponseEntity<DietPlanEntity> update(
            @PathVariable Long planId,
            @RequestBody DietPlanEntity body) {

        return dietPlanService.update(planId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/api/diet-plans/{planId}")
    public ResponseEntity<Void> delete(@PathVariable Long planId) {
        dietPlanService.delete(planId);
        return ResponseEntity.noContent().build();
    }
}
