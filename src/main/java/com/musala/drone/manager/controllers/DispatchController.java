package com.musala.drone.manager.controllers;

import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.misc.Utils;
import com.musala.drone.manager.services.interfaces.DroneServiceInterface;
import com.musala.drone.manager.services.interfaces.MedicationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class DispatchController {

    @Autowired
    DroneServiceInterface droneService;

    @Autowired
    Utils utils;

    @Autowired
    MedicationServiceInterface medicationServiceInterface;

    //Register a drone
    @PostMapping(value = "/drone/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Response registerDrone(@RequestBody DroneDto request) {

        Response validationResponse = utils.validateDrone(request);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.createDrone(request);
    }

    //Load drone with Medication
    @PostMapping(value = "/drone/medication/new", produces = APPLICATION_JSON_VALUE)
    public Response loadMedication(@RequestBody MedicationDto medicationDto, @RequestParam String id) {

        Response validationResponse = utils.validateMedication(medicationDto);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        Response idValidationResponse = utils.validateIds(id);

        if (idValidationResponse.getStatusCode().equalsIgnoreCase("1")){
            return idValidationResponse;
        }

        return droneService.loadDroneWithNewMedication(medicationDto, Long.parseLong(id));
    }

    //check medication loaded on drone
    @PostMapping(value = "/drone/check_medication", produces = APPLICATION_JSON_VALUE)
    public Response checkMedication(@RequestParam String id) {

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.checkMedicationOnDrone(Long.parseLong(id));
    }

    //get drone in idle or loading state in otherwords get available drones for loading
    @PostMapping(value = "/drone/available/get", produces = APPLICATION_JSON_VALUE)
    public Response getAvailableDrone(){
        return droneService.getAllAvailableDrones();
    }


    //get battery level of drone
    @PostMapping(value = "/drone/battery/level", produces = APPLICATION_JSON_VALUE)
    public Response getBatteryLevel(@RequestParam String id){

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.getBatteryLevel(Long.parseLong(id));
    }

    @PostMapping(value = "/drone/get", produces = APPLICATION_JSON_VALUE)
    public Response getDrone(@RequestParam String id){

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.getDrone(Long.parseLong(id));
    }

    @PostMapping(value = "/drone/all", produces = APPLICATION_JSON_VALUE)
    public Response getAllDrones(){
        return droneService.getAllDrones();
    }

    @PostMapping(value = "/drone/medication/joinById", produces = APPLICATION_JSON_VALUE)
    public Response tieExistingMedicationToDrone(@RequestParam String droneId, @RequestParam String medicationId) {

        //validate input
        Response validationResponse = utils.validateIds(droneId);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        Response medicationResponse = utils.validateIds(droneId);

        if (medicationResponse.getStatusCode().equalsIgnoreCase("1")){
            return medicationResponse;
        }

        return droneService.loadDroneWithExistingMedication(Long.parseLong(medicationId), Long.parseLong(droneId));
    }

    @PostMapping(value = "/drone/getBySerial")
    public Response getDroneBySerialNumber(@RequestParam String serialNumber){

        //validate input
        Response validationResponse = utils.validateIds(serialNumber);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.getDroneBySerialNumber(serialNumber);
    }

    @PostMapping(value = "/drone/status/update")
    public Response updateDroneStatus(@RequestParam String id){

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.updateDroneStatusByOneStep(Long.parseLong(id));
    }

    @PostMapping(value = "/drone/update")
    public Response updateDrone(@RequestBody DroneDto droneDto, @RequestParam String id) {

        Response validationResponse = utils.validateDrone(droneDto);
        Response idValidationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        if (idValidationResponse.getStatusCode().equalsIgnoreCase("1")){
            return idValidationResponse;
        }

        return droneService.updateDrone(droneDto, Long.parseLong(id));
    }

    @PostMapping(value = "/drone/delete")
    public Response deleteDrone(@RequestParam String id){

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return droneService.deleteDrone(Long.parseLong(id));
    }

}
