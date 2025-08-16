package com.zoo.animal;

public class AnimalDTO {
    private Long id;
    private String name;
    private String species;
    private String habitat;
    private String diet;
    private String originCountry;
    private int age;
    private String gender;
    private boolean canSwim;
    private boolean canFly;

    public AnimalDTO() {
    }

    public AnimalDTO(Long id, String name, String species, String habitat, String diet,
                     String originCountry, int age, String gender, boolean canSwim, boolean canFly) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.habitat = habitat;
        this.diet = diet;
        this.originCountry = originCountry;
        this.age = age;
        this.gender = gender;
        this.canSwim = canSwim;
        this.canFly = canFly;
    }

    // Getter ve Setter'lar burada...


    public Long getId() {
        return id;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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
