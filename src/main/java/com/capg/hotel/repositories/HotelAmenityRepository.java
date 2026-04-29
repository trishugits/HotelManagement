package com.capg.hotel.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.capg.hotel.dtos.HotelAmenityId;
import com.capg.hotel.entities.HotelAmenity;
import com.capg.hotel.projections.AmenityHotelsProjection;

@RepositoryRestResource(
        path = "hotelamenities",
        collectionResourceRel = "hotelamenities",
        excerptProjection = AmenityHotelsProjection.class
)
interface HotelAmenityRepository extends JpaRepository<HotelAmenity,HotelAmenityId> {
    List<HotelAmenity> findByAmenityAmenityId(Integer amenityId);
    List<HotelAmenity> findByHotelHotelId(Integer hotelId);
}