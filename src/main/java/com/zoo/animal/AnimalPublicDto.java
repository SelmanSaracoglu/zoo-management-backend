package com.zoo.animal;

public class AnimalPublicDto {
    private Long id;
    private String name;
    private String species;
    private Integer age;
    private String habitat;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getSpecies() {return species;}
    public void setSpecies(String species) {this.species = species;}

    public Integer getAge() {return age;}
    public void setAge(Integer age) {this.age = age;}

    public String getHabitat() {return habitat;}
    public void setHabitat(String habitat) {this.habitat = habitat;}

}
