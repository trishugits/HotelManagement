package com.capg.hotel.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import com.capg.hotel.entities.Hotel;
import com.capg.hotel.projections.HotelProjections;
import org.springframework.data.domain.*;

@RepositoryRestResource(
        path = "hotels",
        collectionResourceRel = "hotels",
        excerptProjection = HotelProjections.class
)
public interface HotelRepository extends JpaRepository<Hotel, Integer> {

    @RestResource(exported = false)
    void deleteById(int id);

    @RestResource(exported = false)
    void delete(Hotel hotel);

    Page<Hotel> findByName(String name,Pageable pageable);
    Page<Hotel> findByLocation(String location ,Pageable pageable);
}