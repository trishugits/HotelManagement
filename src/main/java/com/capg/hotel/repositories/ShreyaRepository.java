package com.capg.hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.capg.hotel.entities.RoomType;	

@RepositoryRestResource(
        path = "roomtypes",
        collectionResourceRel = "roomtypes"
)
public interface ShreyaRepository extends JpaRepository<RoomType, Integer> {

}