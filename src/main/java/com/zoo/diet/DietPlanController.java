package com.zoo.diet;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DietPlanController {
    private final DietPlanService dietPlanService;

    public DietPlanController (DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    // Create plan for an animal (DTO'suz)
    @PostMapping("/animals/{animalId}/diet-plans")
    public ResponseEntity<DietPlanEntity> createForAnimal(
            @PathVariable Long animalId,
            @RequestBody DietPlanEntity body) {

        DietPlanEntity created = dietPlanService.createForAnimal(animalId, body);
        return ResponseEntity.ok(created);
    }

    // List plans by animal
    @GetMapping("/animals/{animalId}/diet-plans")
    public ResponseEntity<List<DietPlanEntity>> listByAnimal(@PathVariable Long animalId) {
        return ResponseEntity.ok(dietPlanService.findByAnimal(animalId));
    }

    @GetMapping("/animals/{animalId}/diet-plans/latest")
    public ResponseEntity<DietPlanEntity> latest(@PathVariable Long animalId) {
        return dietPlanService.latestByAnimal(animalId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build()); // hiç plan yoksa 204
    }

    // Get one
    @GetMapping("/diet-plans/{planId}")
    public ResponseEntity<DietPlanEntity> getOne(@PathVariable Long planId) {
        return dietPlanService.findById(planId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update (ID sabit, animal sabit)
    @PutMapping("/diet-plans/{planId}")
    public ResponseEntity<DietPlanEntity> update(
            @PathVariable Long planId,
            @RequestBody DietPlanEntity body) {

        return dietPlanService.update(planId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/diet-plans/{planId}")
    public ResponseEntity<Void> delete(@PathVariable Long planId) {
        dietPlanService.delete(planId);
        return ResponseEntity.noContent().build();
    }
}
