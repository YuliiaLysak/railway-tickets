package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    Station findByCityAndName(String city, String name);

    @Modifying
    @Transactional
    @Query("UPDATE Station SET city = :city, name = :name WHERE id = :id")
    void updateStationById(
            @Param("id") Long id,
            @Param("city") String city,
            @Param("name") String name
    );

    @Modifying
    @Query("DELETE FROM Station WHERE id = :id")
    void deleteById(@Param("id") Long id);
}
