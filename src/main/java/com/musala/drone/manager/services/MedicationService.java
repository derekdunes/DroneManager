package com.musala.drone.manager.services;

import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.models.Drone;
import com.musala.drone.manager.models.Medication;
import com.musala.drone.manager.repository.MedicationRepository;
import com.musala.drone.manager.services.interfaces.MedicationServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class MedicationService implements MedicationServiceInterface {

    @Autowired
    MedicationRepository medicationRepository;

    @Override
    public Response createMedication(MedicationDto medicationDto) {

        Response response = null;

        try {

            Medication medication = new Medication();
            medication.setImage(medicationDto.getImage());
            medication.setName(medicationDto.getName());
            medication.setWeight(medicationDto.getWeight());
            medication.setCode(medicationDto.getCode());

            medicationRepository.save(medication);

            medicationDto.setId(medication.getId());
            response = new Response("0", "Success", medicationDto);

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;
    }

    @Override
    public Response getAllMedications() {

        Response response = null;

        try {

            List<Medication> medicationList = medicationRepository.findAll();

            response = new Response("0", "Success");
            response.setMedicationList(medicationList);

        } catch (Exception e) {

            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");

        }

        return response;

    }

    @Override
    public Response getMedication(Long medicationId) {

        Response response = null;

        try {

            Optional<Medication> optionalMedication = medicationRepository.findById(medicationId);

            if (optionalMedication.isPresent()){
                Medication medication = optionalMedication.get();

                MedicationDto medicationDto = new MedicationDto();
                medicationDto.setId(medication.getId());
                medicationDto.setCode(medication.getCode());
                medicationDto.setName(medication.getName());
                medicationDto.setWeight(medication.getWeight());
                medicationDto.setImage(medication.getImage());
                medicationDto.setDrone(medication.getDrone());

                response = new Response("0", "Success", medicationDto);

            } else {
                response = new Response("1", "No Medication found with this ID " + medicationId);
            }

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;

    }

    @Override
    public Response updateMedication(MedicationDto medicationDto, Long medicationId) {

        Response response = null;

        try {

            Optional<Medication> optionalMedication = medicationRepository.findById(medicationId);

            if (optionalMedication.isPresent()){
                Medication medication = optionalMedication.get();

                if(medicationDto.getCode() != null)
                    medication.setCode(medicationDto.getCode());

                if(medicationDto.getImage()  != null)
                    medication.setImage(medicationDto.getImage());

                if(medicationDto.getWeight() != 0)
                    medication.setWeight(medicationDto.getWeight());

                if(medicationDto.getName() != null)
                    medication.setName(medicationDto.getName());

                medicationRepository.save(medication);

                response = new Response("0", "Success");
            } else {
                response = new Response("1", "No Medication found with this ID " + medicationId);
            }

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;
    }

    @Override
    public Response deleteMedication(Long medicationId) {

        Response response = null;

        try {

           Optional<Medication> medicationOptional =  medicationRepository.findById(medicationId);

           if (medicationOptional.isPresent()){
               Medication medication = medicationOptional.get();

               if (medication.getDrone() == null) {//no Drone tied to it yet
                   medicationRepository.deleteById(medicationId);

                   response = new Response("0", "Success");

               } else {
                   //not null meaning still tied
                   Drone drone = medication.getDrone();

                   Medication medication1 = drone.getMedication();

                   //if medication ID matches with our deleteMedication id don't delete else delete
                   if (Objects.equals(medication1.getId(), medicationId)){
                       //don't delete
                       response = new Response("1", "Cannot delete Id, still tied" + medicationId);
                   } else {
                       //untied
                       medicationRepository.deleteById(medicationId);
                       response = new Response("0", "Success");
                   }
               }

           } else {
               response = new Response("1", "No Medication found with this ID " + medicationId);
           }

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;
    }

    @Override
    public Response checkDroneCarryingMedication(Long medicationId) {

        Response response = null;

        try {

            Optional<Medication> medicationOptional = medicationRepository.findById(medicationId);

            if (medicationOptional.isPresent()) {

                Medication medication = medicationOptional.get();

                MedicationDto medicationDto = new MedicationDto();
                medicationDto.setDrone(medication.getDrone());
                medicationDto.setCode(medication.getCode());
                medicationDto.setImage(medication.getImage());
                medicationDto.setName(medication.getName());
                medicationDto.setWeight(medication.getWeight());

                response = new Response("0", "Success", medicationDto);

            } else {
                response = new Response("1", "No Medication found with this ID " + medicationId);
            }

        } catch (Exception e) {
            log.error("Error while saving drone", e);
            response = new Response("1", "Error while saving Drone to the database");
        }

        return response;
    }
}
