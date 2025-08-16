package com.zoo.feed;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
public class FeedLogController {

    private final FeedLogService feedLogService;

    public FeedLogController(FeedLogService feedLogService) {
        this.feedLogService = feedLogService;
    }

    // Create feed log for an animal
    @PostMapping("api/animals/{animalId}/feeds")
    public ResponseEntity<FeedLogEntity> create(
            @PathVariable Long animalId,
            @RequestBody FeedLogEntity body
    ) {
        return ResponseEntity.ok(feedLogService.createForAnimal(animalId, body));
    }

    // List feeds by animal (optional date range)
    @GetMapping("/api/animals/{animalId}/feeds")
    public ResponseEntity<List<FeedLogEntity>> list(
            @PathVariable Long animalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        if (from != null && to != null) {
            return ResponseEntity.ok(feedLogService.listByAnimalAndRange(animalId, from, to));
        }
        return ResponseEntity.ok(feedLogService.listByAnimal(animalId));
    }

    // Get one
    @GetMapping("/api/feeds/{feedId}")
    public ResponseEntity<FeedLogEntity> getOne(@PathVariable Long feedId) {
        return feedLogService.findById(feedId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/api/feeds/{feedId}")
    public ResponseEntity<FeedLogEntity> update(
            @PathVariable Long feedId,
            @RequestBody FeedLogEntity body
    ) {
        return feedLogService.update(feedId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/api/feeds/{feedId}")
    public ResponseEntity<Void> delete(@PathVariable Long feedId) {
        feedLogService.delete(feedId);
        return ResponseEntity.noContent().build();
    }

}
