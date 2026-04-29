package com.capg.hotel.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<HotelAmenity> findByAmenityAmenityId(Integer amenityId, Pageable pageable);
    Page<HotelAmenity> findByHotelHotelId(Integer hotelId, Pageable pageable);
}