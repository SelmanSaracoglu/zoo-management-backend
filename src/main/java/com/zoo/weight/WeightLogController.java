// src/main/java/com/zoo/weight/WeightLogController.java
package com.zoo.weight;

import com.zoo.weight.WeightLogDto;
import com.zoo.weight.WeightLogMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class WeightLogController {

    private final WeightLogService service;
    public WeightLogController(WeightLogService service){ this.service = service; }

    // POST /api/v1/animals/{animalId}/weight-logs
    @PostMapping("/animals/{animalId}/weight-logs")
    public ResponseEntity<WeightLogDto> create(@PathVariable Long animalId,
                                               @RequestBody @Valid WeightLogDto body){
        if (body.getAnimalId() == null || !body.getAnimalId().equals(animalId)) {
            return ResponseEntity.badRequest().build();
        }
        WeightLogEntity created = service.create(animalId, toEntity(body));
        WeightLogDto out = WeightLogMapper.toDto(created);
        return ResponseEntity.created(URI.create("/api/v1/weight-logs/" + out.getId())).body(out);
    }

    // GET /api/v1/animals/{animalId}/weight-logs?page=&size=&sort=&from=&to=&minKg=&maxKg=
    @GetMapping("/animals/{animalId}/weight-logs")
    public ResponseEntity<Page<WeightLogDto>> list(
            @PathVariable Long animalId,
            @RequestParam(required=false) LocalDateTime from,
            @RequestParam(required=false) LocalDateTime to,
            @RequestParam(required=false) BigDecimal minKg,
            @RequestParam(required=false) BigDecimal maxKg,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="measuredAt,desc") String sort) {

        String[] s = sort.split(",");
        String prop = (s.length>0 ? s[0] : "measuredAt");
        if (!prop.equals("measuredAt") && !prop.equals("id") && !prop.equals("weightKg")) prop = "measuredAt";
        Sort.Direction dir = (s.length>1 && "asc".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));
        Page<WeightLogDto> out = service
                .list(animalId, from, to, minKg, maxKg, pageable)
                .map(WeightLogMapper::toDto);

        return ResponseEntity.ok(out);
    }

    // GET /api/v1/weight-logs/{id}
    @GetMapping("/weight-logs/{id}")
    public ResponseEntity<WeightLogDto> get(@PathVariable Long id){
        return service.get(id)
                .map(e -> ResponseEntity.ok(WeightLogMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/v1/weight-logs/{id}
    @PutMapping("/weight-logs/{id}")
    public ResponseEntity<WeightLogDto> update(@PathVariable Long id, @RequestBody @Valid WeightLogDto body){
        return service.update(id, toEntity(body))
                .map(e -> ResponseEntity.ok(WeightLogMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/weight-logs/{id}
    @DeleteMapping("/weight-logs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // helper
    private WeightLogEntity toEntity(WeightLogDto d){
        WeightLogEntity e = new WeightLogEntity();
        WeightLogMapper.copyToEntity(d, e);
        return e;
    }
}
