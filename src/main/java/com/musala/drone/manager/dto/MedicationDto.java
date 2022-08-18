package com.musala.drone.manager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.musala.drone.manager.models.Drone;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.OneToOne;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MedicationDto {

    private Long id;

    private String name;

    private int weight;

    private String code;

    private String image;

    private Drone drone;

}
