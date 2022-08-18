package com.musala.drone.manager.services.interfaces;

import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.dto.MedicationDto;

public interface DroneServiceInterface {

    public Response createDrone(DroneDto droneDto);

    //update Drone
    public Response updateDrone(DroneDto droneDto, Long droneId);

    //delete Drone
    public Response deleteDrone(Long droneId);

    //getDroneStatus
    public Response updateDroneStatusByOneStep(Long droneId);

    public Response loadDroneWithNewMedication(MedicationDto medicationdto, Long droneId);

    public Response loadDroneWithExistingMedication(Long medicationId, Long droneId);

    public Response checkMedicationOnDrone(Long droneId);

    //get Drone By Serial Number
    public Response getDroneBySerialNumber(String serialNumber);

    public Response getAllAvailableDrones();

    public Response getAllDrones();

    public Response getDrone(Long droneId);

    public Response getBatteryLevel(Long droneId);

}
