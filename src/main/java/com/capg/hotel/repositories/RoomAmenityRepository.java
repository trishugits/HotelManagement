package com.capg.hotel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<RoomAmenity> findByAmenityAmenityId(Integer amenityId, Pageable pageable);
}