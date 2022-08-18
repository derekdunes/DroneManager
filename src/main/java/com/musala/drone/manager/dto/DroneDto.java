package com.musala.drone.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import com.musala.drone.manager.models.Medication;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DroneDto {

    private Long id;

    private String serialNumber;

    private String model;

    private int weight;

    private int batteryCapacity;

    private String state;

    private Medication medication;



}
