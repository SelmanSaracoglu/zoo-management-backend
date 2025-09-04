// src/main/java/com/zoo/vet/VetVisitController.java
package com.zoo.vet;

import com.zoo.vet.VetVisitDto;
import com.zoo.vet.VetVisitMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class VetVisitController {

    private final VetVisitService service;
    public VetVisitController(VetVisitService service){ this.service = service; }

    // POST /api/v1/animals/{animalId}/vet-visits
    @PostMapping("/animals/{animalId}/vet-visits")
    public ResponseEntity<VetVisitDto> create(@PathVariable Long animalId, @RequestBody @Valid VetVisitDto body){
        if (body.getAnimalId() == null || !body.getAnimalId().equals(animalId)) {
            return ResponseEntity.badRequest().build();
        }
        VetVisitEntity created = service.create(animalId, toEntity(body));
        VetVisitDto out = VetVisitMapper.toDto(created);
        return ResponseEntity.created(URI.create("/api/v1/vet-visits/" + out.getId())).body(out);
    }

    // GET /api/v1/animals/{animalId}/vet-visits?... (page/sort/filters)
    @GetMapping("/animals/{animalId}/vet-visits")
    public ResponseEntity<Page<VetVisitDto>> list(
            @PathVariable Long animalId,
            @RequestParam(required=false) String purposeLike,
            @RequestParam(required=false) String diagnosisLike,
            @RequestParam(required=false) LocalDateTime from,
            @RequestParam(required=false) LocalDateTime to,
            @RequestParam(required=false) LocalDate followUpFrom,
            @RequestParam(required=false) LocalDate followUpTo,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="visitTime,desc") String sort) {

        String[] s = sort.split(",");
        String prop = (s.length > 0 ? s[0] : "visitTime");
        if (!prop.equals("visitTime") && !prop.equals("id")) prop = "visitTime";
        Sort.Direction dir = (s.length > 1 && "asc".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));

        Page<VetVisitDto> out = service
                .list(animalId, purposeLike, diagnosisLike, from, to, followUpFrom, followUpTo, pageable)
                .map(VetVisitMapper::toDto);

        return ResponseEntity.ok(out);
    }

    // GET /api/v1/vet-visits/{id}
    @GetMapping("/vet-visits/{id}")
    public ResponseEntity<VetVisitDto> get(@PathVariable Long id){
        return service.get(id).map(e -> ResponseEntity.ok(VetVisitMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/v1/vet-visits/{id}
    @PutMapping("/vet-visits/{id}")
    public ResponseEntity<VetVisitDto> update(@PathVariable Long id, @RequestBody @Valid VetVisitDto body){
        return service.update(id, toEntity(body))
                .map(e -> ResponseEntity.ok(VetVisitMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/vet-visits/{id}
    @DeleteMapping("/vet-visits/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // helper
    private VetVisitEntity toEntity(VetVisitDto d){
        VetVisitEntity e = new VetVisitEntity();
        VetVisitMapper.copyToEntity(d, e);
        return e;
    }
}
