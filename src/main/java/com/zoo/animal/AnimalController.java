package com.zoo.animal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springdoc.core.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/animals")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService){
        this.animalService = animalService;
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> createAnimal(@RequestBody @jakarta.validation.Valid AnimalDTO dto) {
        AnimalDTO created = animalService.createAnimal(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Hayvanları listele")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste döner"),
            @ApiResponse(responseCode = "401", description = "Token yok/geçersiz"),
            @ApiResponse(responseCode = "403", description = "Yetki yok (varsa)")
    })

    @GetMapping
    public ResponseEntity<Page<AnimalDTO>> getAnimals(
            @ParameterObject
            @PageableDefault(size = 10) Pageable pageable  // varsayılan 10 kayıt
    ){
        return ResponseEntity.ok(animalService.getAnimalsPage(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> getAnimalById(@PathVariable Long id){
        return  animalService.getAnimalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> updateAnimal(@PathVariable Long id, @RequestBody @jakarta.validation.Valid AnimalDTO dto) {
        return animalService.updateAnimal(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable Long id) {
        animalService.deleteAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
