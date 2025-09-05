package com.zoo.animal;


import jakarta.persistence.*;

@Entity
@Table(
    name = "animals",
    indexes = {
        @Index(name = "idx_animals_species", columnList = "species"),
        @Index(name = "idx_animals_origin_country", columnList = "origin_country")
    }
)
public class AnimalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long id;

    @Column(length = 120, nullable = false)
    private String name;

    @Column(length = 120, nullable = false)
    private String species;

    @Column(length = 120)
    private String habitat;

    @Column(length = 120)
    private String diet;

    @Column(name = "origin_country", length = 120)
    private String originCountry;

    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(length = 16, nullable = false)
    private Gender gender = Gender.UNKNOWN;

    private boolean canSwim;
    private boolean canFly;

    public AnimalEntity() {}

    public AnimalEntity(Long id,
                        String name,
                        String species,
                        String habitat,
                        String diet,
                        String originCountry,
                        Integer age,
                        Gender gender,
                        boolean canSwim,
                        boolean canFly) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.habitat = habitat;
        this.diet = diet;
        this.originCountry = originCountry;
        this.age = age;
        this.gender = gender == null ? Gender.UNKNOWN : gender;
        this.canSwim = canSwim;
        this.canFly = canFly;
    }

    public Long getId() { return id; }
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = (gender == null ? Gender.UNKNOWN : gender);
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
