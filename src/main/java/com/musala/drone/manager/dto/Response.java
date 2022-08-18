package com.musala.drone.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musala.drone.manager.models.Drone;
import com.musala.drone.manager.models.Medication;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {

    private String statusCode;
    private String statusMessage;
    private DroneDto drone;
    private MedicationDto medication;
    private List<Drone> droneList;
    private List<Medication> medicationList;



    public Response(String statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public Response(String statusCode, String statusMessage, DroneDto drone) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.drone = drone;
    }

    public Response(String statusCode, String statusMessage, MedicationDto medication) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.medication = medication;
    }

    public Response(String statusCode, String statusMessage, List<Drone> droneList) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.droneList = droneList;
    }
}
