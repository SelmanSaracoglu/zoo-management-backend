package com.zoo.feed;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FeedLogController {

    private final FeedLogService feedLogService;

    public FeedLogController(FeedLogService feedLogService) {
        this.feedLogService = feedLogService;
    }

    // Create feed log for an animal
    @PostMapping("/animals/{animalId}/feeds")
    public ResponseEntity<FeedLogEntity> create(@PathVariable Long animalId,
                                                @RequestBody FeedLogEntity body) {
        return ResponseEntity.ok(feedLogService.createForAnimal(animalId, body));
    }

    @GetMapping("/animals/{animalId}/feeds")
    public ResponseEntity<List<FeedLogEntity>> list( @PathVariable Long animalId ) {
        return ResponseEntity.ok(feedLogService.listByAnimal(animalId));
    }

    @GetMapping("/animals/{animalId}/feeds/latest")
    public ResponseEntity<FeedLogEntity> latest(@PathVariable Long animalId) {
        return feedLogService.latestByAnimal(animalId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    // Get one
    @GetMapping("/feeds/{feedId}")
    public ResponseEntity<FeedLogEntity> getOne(@PathVariable Long feedId) {
        return feedLogService.findById(feedId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/feeds/{feedId}")
    public ResponseEntity<FeedLogEntity> update(
            @PathVariable Long feedId,
            @RequestBody FeedLogEntity body
    ) {
        return feedLogService.update(feedId, body)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete
    @DeleteMapping("/feeds/{feedId}")
    public ResponseEntity<Void> delete(@PathVariable Long feedId) {
        feedLogService.delete(feedId);
        return ResponseEntity.noContent().build();
    }

}
