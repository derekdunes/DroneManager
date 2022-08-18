package com.musala.drone.manager.repository;

import com.musala.drone.manager.enums.State;
import com.musala.drone.manager.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    //write query to get Drone with status not    LOADING,//    LOADED,//    DELIVERING,
    @Query(value = "SELECT u FROM Drone u WHERE u.state in (:idle, :loading)")
    List<Drone> findIdleDrones(@Param("idle") State idleState, @Param("loading") State loadingState);

    Optional<Drone> findBySerialNumber(String serialNumber);

}
