package com.zoo.staff;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class StaffController {

    private final StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<StaffEntity> create(@RequestBody StaffEntity body) {
        return ResponseEntity.ok(staffService.create(body));
    }

    @GetMapping
    public ResponseEntity<List<StaffEntity>> list() {
        return ResponseEntity.ok(staffService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffEntity> get(@PathVariable Long id) {
        return staffService.get(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffEntity> update(@PathVariable Long id, @RequestBody StaffEntity body) {
        return staffService.update(id, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        staffService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
