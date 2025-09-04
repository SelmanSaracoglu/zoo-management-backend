package com.zoo.diet;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DietPlanController {
    private final DietPlanService dietPlanService;

    public DietPlanController (DietPlanService dietPlanService) {
        this.dietPlanService = dietPlanService;
    }

    // POST /api/v1/animals/{animalId}/diet-plans
    @PostMapping("/animals/{animalId}/diet-plans")
    public ResponseEntity<DietPlanDto> createForAnimal(
            @PathVariable Long animalId,
            @RequestBody @Valid DietPlanDto body) {
        body.setAnimalId(animalId);
        // Servis entity bekliyorsa: boş entity oluşturup DTO’dan doldur
        DietPlanEntity toCreate = DietPlanMapper.toEntity(body, new DietPlanEntity());
        DietPlanEntity created = dietPlanService.createForAnimal(animalId, toCreate);

        DietPlanDto out = DietPlanMapper.toDto(created);
        return ResponseEntity
                .created(URI.create("/api/v1/diet-plans/" + out.getId()))
                .body(out);
    }

    // GET /api/v1/animals/{animalId}/diet-plans
    @GetMapping("/animals/{animalId}/diet-plans")
    public ResponseEntity<Page<DietPlanDto>> listByAnimal(
            @PathVariable Long animalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startDate,desc") String sort) {

        // güvenli sıralama alanları
        String[] arr = sort.split(",");
        String prop = (arr.length > 0 ? arr[0] : "startDate");
        if (!prop.equals("startDate") && !prop.equals("endDate") && !prop.equals("id")) {
            prop = "startDate";
        }
        Sort.Direction dir = (arr.length > 1 && "asc".equalsIgnoreCase(arr[1]))
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));

        Page<DietPlanDto> out = dietPlanService
                .findByAnimal(animalId, pageable)
                .map(DietPlanMapper::toDto);

        return ResponseEntity.ok(out);
    }

    // GET /api/v1/animals/{animalId}/diet-plans/latest
    @GetMapping("/animals/{animalId}/diet-plans/latest")
    public ResponseEntity<DietPlanDto> latest(@PathVariable Long animalId) {
        return dietPlanService.latestByAnimal(animalId)
                .map(e -> ResponseEntity.ok(DietPlanMapper.toDto(e)))
                .orElseGet(() -> ResponseEntity.noContent().build()); // hiç plan yoksa 204
    }

    // GET /api/v1/diet-plans/{planId}
    @GetMapping("/diet-plans/{planId}")
    public ResponseEntity<DietPlanDto> getOne(@PathVariable Long planId) {
        return dietPlanService.findById(planId)
                .map(e -> ResponseEntity.ok(DietPlanMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }


    // PUT /api/v1/diet-plans/{planId}
    @PutMapping("/diet-plans/{planId}")
    public ResponseEntity<DietPlanDto> update(
            @PathVariable Long planId,
            @RequestBody @Valid DietPlanDto body) {

        // Servis update(entity) döndürüyor kabul edelim
        return dietPlanService.update(planId, DietPlanMapper.toEntity(body, new DietPlanEntity()))
                .map(e -> ResponseEntity.ok(DietPlanMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/diet-plans/{planId}
    @DeleteMapping("/diet-plans/{planId}")
    public ResponseEntity<Void> delete(@PathVariable Long planId) {
        dietPlanService.delete(planId);
        return ResponseEntity.noContent().build();
    }
}
