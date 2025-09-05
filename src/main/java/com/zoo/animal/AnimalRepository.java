package com.zoo.animal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepository extends JpaRepository<AnimalEntity, Long>, JpaSpecificationExecutor<AnimalEntity> {
    boolean existsByNameIgnoreCase(String name);
}
