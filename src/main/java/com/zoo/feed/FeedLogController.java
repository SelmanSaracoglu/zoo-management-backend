package com.zoo.feed;


import com.zoo.feed.FeedLogDto;
import com.zoo.feed.FeedLogMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
public class FeedLogController {

    private final FeedLogService service;

    public FeedLogController(FeedLogService service) {
        this.service = service;
    }

    // POST /api/v1/animals/{animalId}/feed-logs
    @PostMapping("/animals/{animalId}/feed-logs")
    public ResponseEntity<FeedLogDto> create(@PathVariable Long animalId, @RequestBody @Valid FeedLogDto body){
        if (body.getAnimalId() == null || !body.getAnimalId().equals(animalId)) {
            return ResponseEntity.badRequest().build();
        }
        FeedLogEntity created = service.create(animalId, toEntity(body));
        FeedLogDto out = FeedLogMapper.toDto(created);
        return ResponseEntity.created(URI.create("/api/v1/feed-logs/" + out.getId())).body(out);
    }

    // GET /api/v1/animals/{animalId}/feed-logs?page=&size=&sort=&foodLike=&from=&to=
    @GetMapping("/animals/{animalId}/feed-logs")
    public ResponseEntity<Page<FeedLogDto>> list(
            @PathVariable Long animalId,
            @RequestParam(required=false) String foodLike,
            @RequestParam(required=false) LocalDateTime from,
            @RequestParam(required=false) LocalDateTime to,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="feedTime,desc") String sort) {

        String[] s = sort.split(",");
        String prop = (s.length>0 ? s[0] : "feedTime");
        if (!prop.equals("feedTime") && !prop.equals("id") && !prop.equals("foodItem")) prop = "feedTime";
        Sort.Direction dir = (s.length>1 && "asc".equalsIgnoreCase(s[1])) ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));
        Page<FeedLogDto> out = service.list(animalId, foodLike, from, to, pageable).map(FeedLogMapper::toDto);
        return ResponseEntity.ok(out);
    }

    // GET /api/v1/feed-logs/{id}
    @GetMapping("/feed-logs/{id}")
    public ResponseEntity<FeedLogDto> get(@PathVariable Long id){
        return service.get(id).map(e -> ResponseEntity.ok(FeedLogMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/v1/feed-logs/{id}
    @PutMapping("/feed-logs/{id}")
    public ResponseEntity<FeedLogDto> update(@PathVariable Long id, @RequestBody @Valid FeedLogDto body){
        return service.update(id, toEntity(body))
                .map(e -> ResponseEntity.ok(FeedLogMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/v1/feed-logs/{id}
    @DeleteMapping("/feed-logs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // helper
    private FeedLogEntity toEntity(FeedLogDto d){
        FeedLogEntity e = new FeedLogEntity();
        e.setFeedTime(d.getFeedTime());
        e.setFoodItem(d.getFoodItem());
        e.setQuantityGrams(d.getQuantityGrams());
        e.setWaterMilliliters(d.getWaterMilliliters());
        e.setNotes(d.getNotes());
        return e;
    }

}
