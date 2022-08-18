package com.musala.drone.manager.services.interfaces;

import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.dto.MedicationDto;

public interface MedicationServiceInterface {

    //create medication
    public Response createMedication(MedicationDto  medicationDto);

    //get medications
    public Response getAllMedications();

    //get one medication
    public Response getMedication(Long medicationId);

    //update medication
    public Response updateMedication(MedicationDto medicationDto, Long medicationId);

    //delete medication
    public Response deleteMedication(Long medicationId);

    public Response checkDroneCarryingMedication(Long medicationId);

}
