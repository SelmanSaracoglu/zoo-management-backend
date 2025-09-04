// src/main/java/com/zoo/medication/MedicationController.java
package com.zoo.medication;

import com.zoo.medication.MedicationDto;
import com.zoo.medication.MedicationMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class MedicationController {

    private final MedicationService service;

    public MedicationController(MedicationService service) { this.service = service; }

    // POST /api/v1/animals/{animalId}/medications
    @PostMapping("/animals/{animalId}/medications")
    public ResponseEntity<MedicationDto> create(@PathVariable Long animalId,
                                                @RequestBody @Valid MedicationDto body) {
        if (body.getAnimalId() == null || !body.getAnimalId().equals(animalId)) {
            return ResponseEntity.badRequest().build();
        }
        MedicationEntity created = service.create(animalId, toEntity(body));
        MedicationDto out = MedicationMapper.toDto(created);
        return ResponseEntity.created(URI.create("/api/v1/medications/" + out.getId())).body(out);
    }

    // GET /api/v1/animals/{animalId}/medications?page=&size=&sort=&medLike=&route=&from=&to=
    @GetMapping("/animals/{animalId}/medications")
    public ResponseEntity<Page<MedicationDto>> list(
            @PathVariable Long animalId,
            @RequestParam(required = false) String medLike,
            @RequestParam(required = false) String route,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "givenAt,desc") String sort) {

        String[] s = sort.split(",");
        String prop = (s.length > 0 ? s[0] : "givenAt");
        if (!prop.equals("givenAt") && !prop.equals("id") && !prop.equals("medName")) prop = "givenAt";
        Sort.Direction dir = (s.length > 1 && "asc".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));

        Page<MedicationDto> out = service
                .list(animalId, medLike, route, from, to, pageable)
                .map(MedicationMapper::toDto);

        return ResponseEntity.ok(out);
    }

    // GET /api/v1/medications/{id}
    @GetMapping("/medications/{id}")
    public ResponseEntity<MedicationDto> get(@PathVariable Long id) {
        return service.get(id)
                .map(e -> ResponseEntity.ok(MedicationMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/v1/medications/{id}
    @PutMapping("/medications/{id}")
    public ResponseEntity<MedicationDto> update(@PathVariable Long id,
                                                @RequestBody @Valid MedicationDto body) {
        return service.update(id, toEntity(body))
                .map(e -> ResponseEntity.ok(MedicationMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/medications/{id}
    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- helper ---
    private MedicationEntity toEntity(MedicationDto d){
        MedicationEntity e = new MedicationEntity();
        MedicationMapper.copyToEntity(d, e);
        return e;
    }
}
