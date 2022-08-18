package com.musala.drone.manager.controllers;

import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.misc.Utils;
import com.musala.drone.manager.services.interfaces.DroneServiceInterface;
import com.musala.drone.manager.services.interfaces.MedicationServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MedicationController {

    @Autowired
    DroneServiceInterface droneService;

    @Autowired
    Utils utils;

    @Autowired
    MedicationServiceInterface medicationService;

    @PostMapping(value = "/medication/carrier/get", produces = APPLICATION_JSON_VALUE)
    public Response checkDroneCarryingMedication(String id){

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return medicationService.checkDroneCarryingMedication(Long.parseLong(id));
    }


    @PostMapping(value = "/medication/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Response createMedication(@RequestBody MedicationDto request) {

        //validate input
        Response validationResponse = utils.validateMedication(request);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return medicationService.createMedication(request);
    }

    @PostMapping(value = "/medication/get", produces = APPLICATION_JSON_VALUE)
    public Response getMedication(@RequestParam String id) {

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return medicationService.getMedication(Long.parseLong(id));
    }


    @PostMapping(value = "/medication/update", produces = APPLICATION_JSON_VALUE)
    public Response updateMedication(@RequestBody MedicationDto medicationDto, @RequestParam String id) {

        Response validationResponse = utils.validateMedication(medicationDto);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        //validate input
        Response idValidationResponse = utils.validateIds(id);

        if (idValidationResponse.getStatusCode().equalsIgnoreCase("1")){
            return idValidationResponse;
        }

        return medicationService.updateMedication(medicationDto, Long.parseLong(id));
    }

    @PostMapping(value = "/medication/delete", produces = APPLICATION_JSON_VALUE)
    public Response deleteMedication(@RequestParam String id){

        //validate input
        Response validationResponse = utils.validateIds(id);

        if (validationResponse.getStatusCode().equalsIgnoreCase("1")){
            return validationResponse;
        }

        return medicationService.deleteMedication(Long.parseLong(id));
    }



}
