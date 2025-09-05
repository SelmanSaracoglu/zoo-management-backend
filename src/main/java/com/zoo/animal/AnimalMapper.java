package com.zoo.animal;

public class AnimalMapper {
    private AnimalMapper(){}

    public static AnimalDto toDto(AnimalEntity e) {
        AnimalDto d = new AnimalDto();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setSpecies(e.getSpecies());
        d.setHabitat(e.getHabitat());
        d.setDiet(e.getDiet());
        d.setOriginCountry(e.getOriginCountry());
        d.setAge(e.getAge());
        d.setGender(e.getGender());
        d.setCanSwim(e.isCanSwim());
        d.setCanFly(e.isCanFly());
        return d;
    }

    public static void copyToEntity(AnimalDto d, AnimalEntity e) {
        e.setName(d.getName());
        e.setSpecies(d.getSpecies());
        e.setHabitat(d.getHabitat());
        e.setDiet(d.getDiet());
        e.setOriginCountry(d.getOriginCountry());
        e.setAge(d.getAge());
        e.setGender(d.getGender());
        e.setCanSwim(d.isCanSwim());
        e.setCanFly(d.isCanFly());
    }

    public static AnimalPublicDto toPublicDto(AnimalEntity e) {
        if (e == null) return null;
        AnimalPublicDto d = new AnimalPublicDto();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setSpecies(e.getSpecies());
        d.setAge(e.getAge());
        d.setHabitat(e.getHabitat());
        return d;
    }
}
