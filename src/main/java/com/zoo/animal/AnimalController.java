// src/main/java/com/zoo/animal/AnimalController.java
package com.zoo.animal;

import com.zoo.animal.AnimalDto;
import com.zoo.animal.AnimalMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/animals")
public class AnimalController {

    private final AnimalService service;
    public AnimalController(AnimalService service){ this.service = service; }

    @PostMapping
    public ResponseEntity<AnimalDto> create(@RequestBody @Valid AnimalDto body){
        AnimalEntity created = service.create(body);
        return ResponseEntity.created(URI.create("/api/v1/animals/"+created.getId()))
                .body(AnimalMapper.toDto(created));
    }

    @GetMapping
    public ResponseEntity<Page<AnimalDto>> list(
            @RequestParam(required=false) String species,
            @RequestParam(required=false) String nameLike,
            @RequestParam(required=false) String originCountry,
            @RequestParam(required=false) Gender gender,
            @RequestParam(required=false) Boolean canSwim,
            @RequestParam(required=false) Boolean canFly,
            @RequestParam(required=false) Integer minAge,
            @RequestParam(required=false) Integer maxAge,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="10") int size,
            @RequestParam(defaultValue="name,asc") String sort){

        String[] s = sort.split(",");
        String prop = (s.length>0 ? s[0] : "name");
        if (!prop.equals("name") && !prop.equals("species") && !prop.equals("age") && !prop.equals("id"))
            prop = "name";
        Sort.Direction dir = (s.length>1 && "desc".equalsIgnoreCase(s[1]))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, prop));
        Page<AnimalDto> out = service
                .list(species, nameLike, originCountry, gender, canSwim, canFly, minAge, maxAge, pageable)
                .map(AnimalMapper::toDto);

        return ResponseEntity.ok(out);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDto> get(@PathVariable Long id){
        return service.get(id).map(e -> ResponseEntity.ok(AnimalMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalDto> update(@PathVariable Long id, @RequestBody @Valid AnimalDto body){
        return service.update(id, body)
                .map(e -> ResponseEntity.ok(AnimalMapper.toDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
