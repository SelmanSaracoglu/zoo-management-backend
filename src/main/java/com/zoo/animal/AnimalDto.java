package com.zoo.animal;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

public class AnimalDto {
    private Long id;

    @NotBlank @Size(max = 120)
    private String name;

    @NotBlank @Size(max = 120)
    private String species;

    @Size(max = 120) private String habitat;
    @Size(max = 120) private String diet;
    @Size(max = 120) private String originCountry;

    @Min(0) @Max(300)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 16)
    private Gender gender;

    private boolean canSwim;
    private boolean canFly;

    public AnimalDto() {
    }

    // Getter ve Setter'lar burada...


    public Long getId() {
        return id;
    }
    public void setId(Long id) { this.id = id; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }

    public String getHabitat() {
        return habitat;
    }
    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getDiet() {
        return diet;
    }
    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getOriginCountry() {
        return originCountry;
    }
    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public boolean isCanSwim() {
        return canSwim;
    }
    public void setCanSwim(boolean canSwim) {
        this.canSwim = canSwim;
    }

    public boolean isCanFly() {
        return canFly;
    }
    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }
}
