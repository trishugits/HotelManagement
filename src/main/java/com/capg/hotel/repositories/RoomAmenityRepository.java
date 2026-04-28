package com.capg.hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.capg.hotel.dtos.RoomAmenityId;
import com.capg.hotel.entities.RoomAmenity;
import com.capg.hotel.projections.AmenityRoomsProjection;

@RepositoryRestResource(
        path = "roomamenities",
        collectionResourceRel = "roomamenities",
        excerptProjection = AmenityRoomsProjection.class
)
interface RoomAmenityRepository extends JpaRepository<RoomAmenity,RoomAmenityId> {
    List<RoomAmenity> findByAmenityAmenityId(Integer amenityId);
}