// src/main/java/com/zoo/staff/StaffController.java
package com.zoo.staff;

import com.zoo.staff.StaffDto;
import com.zoo.staff.StaffMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/staff")
public class StaffController {

    private final StaffService service;
    public StaffController(StaffService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<StaffDto> create(@RequestBody @Valid StaffDto body){
        StaffEntity created = service.create(body);
        StaffDto out = StaffMapper.toDto(created);
        return ResponseEntity.created(URI.create("/api/v1/staff/" + out.getId())).body(out);
    }

    @GetMapping
    public ResponseEntity<Page<StaffDto>> list(
            @RequestParam(required=false) String role,
            @RequestParam(required=false) String nameLike,
            @RequestParam(required=false) String emailLike,
            @RequestParam(required=false) Long animalId,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="id,desc") String sort) {

        String[] s = sort.split(",");
        String prop = (s.length>0 ? s[0] : "id");
        if (!prop.equals("id") && !prop.equals("name") && !prop.equals("role")) prop = "id";
        Sort.Direction dir = (s.length>1 && "asc".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));

        Page<StaffDto> out = service.list(role, nameLike, emailLike, animalId, pageable)
                .map(StaffMapper::toDto);

        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffDto> get(@PathVariable Long id){
        return service.get(id).map(e -> ResponseEntity.ok(StaffMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDto> update(@PathVariable Long id, @RequestBody @Valid StaffDto body){
        return service.update(id, body)
                .map(e -> ResponseEntity.ok(StaffMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
