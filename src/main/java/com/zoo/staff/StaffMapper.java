// src/main/java/com/zoo/staff/mapper/StaffMapper.java
package com.zoo.staff;

import com.zoo.staff.StaffEntity;
import com.zoo.staff.StaffDto;

import java.util.stream.Collectors;

public final class StaffMapper {
    private StaffMapper(){}

    public static StaffDto toDto(StaffEntity e){
        StaffDto d = new StaffDto();
        d.setId(e.getId());
        d.setName(e.getName());
        d.setRole(e.getRole());
        d.setEmail(e.getEmail());
        d.setPhone(e.getPhone());
        d.setAnimalIds(
                e.getAnimals().stream().map(a -> a.getId()).collect(Collectors.toSet())
        );
        return d;
    }

    /** Animals set’i service’te yönetilecek; burada temel alanları kopyala. */
    public static void copyBasics(StaffDto d, StaffEntity e){
        e.setName(d.getName());
        e.setRole(d.getRole());
        e.setEmail(d.getEmail());
        e.setPhone(d.getPhone());
    }
}
