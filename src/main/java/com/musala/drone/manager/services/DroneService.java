package com.musala.drone.manager.services;

import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import com.musala.drone.manager.misc.Utils;
import com.musala.drone.manager.models.Drone;
import com.musala.drone.manager.models.Medication;
import com.musala.drone.manager.repository.DroneRepository;
import com.musala.drone.manager.repository.MedicationRepository;
import com.musala.drone.manager.services.interfaces.DroneServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class DroneService implements DroneServiceInterface {

    @Autowired
    DroneRepository droneRepository;

    @Autowired
    MedicationRepository medicationRepository;

    @Autowired
    Utils utils;

    @Override
    public Response createDrone(DroneDto droneDto) {

        Response response = null;

        try {

            if (droneDto.getWeight() > 500){
                return new Response("1", "Weight provided is greater the Maximum acceptable weight(500grm), Please reduce the weight provided");
            }

            Model model = utils.getModelFromString(droneDto.getModel());

            if (model == Model.INVALID){
                return new Response("1", "INVALID Model type " + droneDto.getModel());
            }

            Drone drone = new Drone();
            drone.setState(State.IDLE);
            drone.setModel(model);
            drone.setSerialNumber(droneDto.getSerialNumber());
            drone.setWeight(droneDto.getWeight());
            drone.setBatteryCapacity(droneDto.getBatteryCapacity());

            droneRepository.save(drone);
            droneDto.setId(drone.getId());
            response = new Response("0", "Success", droneDto);

        } catch (Exception e) {
            log.error("Error while saving drone", e);
            response =  new Response("1", "Error while saving Drone to the database");
        }

        return response;
    }

    @Override
    public Response updateDrone(DroneDto droneDto, Long droneId) {//allows updating the only weight, model, serialNumber, BatteryCapacity

        Response response = null;

        try {

            if (droneDto.getWeight() > 500){
                return new Response("1", "Weight provided is greater the Maximum acceptable weight(500grm), Please reduce the weight provided");
            }

            Optional<Drone> optionalDrone = droneRepository.findById(droneId);

            if (optionalDrone.isPresent()){
                Drone drone = optionalDrone.get();

                if(droneDto.getSerialNumber() != null)
                    drone.setSerialNumber(droneDto.getSerialNumber());

                if(droneDto.getWeight() != 0)
                    drone.setWeight(droneDto.getWeight());

                if(droneDto.getBatteryCapacity() != 0)
                    drone.setBatteryCapacity(droneDto.getBatteryCapacity());

                if (droneDto.getModel() != null) {

                    Model model = utils.getModelFromString(droneDto.getModel());

                    if (model == Model.INVALID){
                        return new Response("1", "INVALID Model type " + droneDto.getModel());
                    }

                    drone.setModel(model);
                }

                droneRepository.save(drone);

                response = new Response("0", "Success");
            } else {
                response = new Response("1", "No Medication found with this ID " + droneId);
            }

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;

    }

    @Override
    public Response deleteDrone(Long droneId) {

        Response response = null;

        try {

            Optional<Drone> droneOptional =  droneRepository.findById(droneId);

            if (droneOptional.isPresent()){
                Drone drone = droneOptional.get();

                if (drone.getMedication() == null) {//no Drone tied to it yet
                    droneRepository.deleteById(droneId);

                    response = new Response("0", "Success");

                } else {
                    response = new Response("1", "Cannot delete Id, still carrying a medication with Id " + drone.getMedication().getId());
                }

            } else {
                response = new Response("1", "No Drone found with this ID " + droneId);
            }


        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;

    }

    //untie medication from drone when drone status is delivered
    @Override
    public Response updateDroneStatusByOneStep(Long droneId) {//validate the state before calling method

        Response response = null;

        try {

            Optional<Drone> optionalDrone =  droneRepository.findById(droneId);

            if (optionalDrone.isPresent()){
                Drone drone = optionalDrone.get();

                if (drone.getState() == State.IDLE){
                    drone.setState(State.LOADING);
                } else if (drone.getState() == State.LOADING) {
                    drone.setState(State.LOADED);
                } else if (drone.getState() == State.LOADED) {
                    drone.setState(State.DELIVERING);
                } else if (drone.getState() == State.DELIVERING) {//remove medication ID from
                    drone.setState(State.DELIVERED);
                    drone.setMedication(null);
                } else if (drone.getState() == State.DELIVERED) {
                    drone.setState(State.RETURNING);
                } else if (drone.getState() == State.RETURNING) {
                    drone.setState(State.IDLE);
                }

                droneRepository.save(drone);

                DroneDto droneDto = new DroneDto();
                droneDto.setSerialNumber(drone.getSerialNumber());
                droneDto.setWeight(droneDto.getWeight());
                droneDto.setModel(droneDto.getModel());
                droneDto.setState(utils.getStateAsString(drone.getState()));
                droneDto.setBatteryCapacity(drone.getBatteryCapacity());
                droneDto.setId(drone.getId());
                droneDto.setMedication(drone.getMedication());

                response = new Response("0", "Success", droneDto);

            } else {
                response = new Response("1", "No Drone found with this ID " + droneId);
            }

        } catch(Exception e){
            log.error("Error while saving drone", e);
            response =  new Response("1", "Error while saving Drone to the database");
        }

        return response;

    }

    @Override
    public Response loadDroneWithNewMedication(MedicationDto medicationdto, Long droneId) {

        Response response = null;

        try {

            //save new medication
            Medication medication = new Medication();
            medication.setName(medicationdto.getName());
            medication.setCode(medicationdto.getCode());
            medication.setWeight(medicationdto.getWeight());
            medication.setImage(medicationdto.getImage());

            //get drone
            Optional<Drone> droneOptional = droneRepository.findById(droneId);

            if (droneOptional.isPresent()) {

                Drone drone = droneOptional.get();

                if (drone.getState() != State.IDLE ) {

                    if (drone.getState() != State.LOADING) {// can only start loading in idle state
                        //drone is not available for loading
                        return new Response("1", "Drone cannot be loaded cause its not Idle");
                    }
                }
                //validate if the drone can carry it
                if (medication.getWeight() >= drone.getWeight()) {
                    //can't carry the medication
                    return new Response("1", "Medication is heavier than the Drone, please pick another drone");
                }

                drone.setMedication(medication);
                drone.setState(State.LOADED);

                droneRepository.save(drone);

                response = new Response("0", "Success");

                medicationdto.setId(medication.getId());
                response.setMedication(medicationdto);

                DroneDto droneDto = new DroneDto();
                droneDto.setState(utils.getStateAsString(drone.getState()));
                droneDto.setMedication(drone.getMedication());
                droneDto.setModel(utils.getStringFromModel(drone.getModel()));
                droneDto.setWeight(drone.getWeight());
                droneDto.setSerialNumber(drone.getSerialNumber());
                droneDto.setId(drone.getId());
                droneDto.setBatteryCapacity(drone.getBatteryCapacity());

                response.setDrone(droneDto);

            } else {
                response = new Response("1", "No Drone found with this ID " + droneId);
            }

        } catch(Exception e){
            log.error("Error while saving drone", e);
            response =  new Response("1", "Error while saving Drone to the database");
        }

        return response;
    }

    @Override
    public Response loadDroneWithExistingMedication(Long medicationId, Long droneId) {

        Response response = null;

        try {
            //save new medication
            Optional<Medication> medicationOptional = medicationRepository.findById(medicationId);

            Optional<Drone> droneOptional = droneRepository.findById(droneId);

            if (medicationOptional.isPresent() && droneOptional.isPresent()) {
                Medication medication = medicationOptional.get();
                Drone drone = droneOptional.get();

                if (drone.getState() != State.IDLE) {// can only start loading in idle state
                    //drone is not available for loading
                    if (drone.getState() != State.LOADING){
                        return new Response("1", "Drone cannot be loaded cause its not Idle");
                    }
                }

                //validate if the drone can carry it
                if (medication.getWeight() >= drone.getWeight()) {
                    //can't carry the medication
                    return new Response("1", "Medication is heavier than the Drone, please pick another drone");
                }

                drone.setMedication(medication);
                drone.setState(State.LOADED);

                droneRepository.save(drone);

                DroneDto droneDto = new DroneDto();
                droneDto.setState(utils.getStateAsString(drone.getState()));
                droneDto.setMedication(drone.getMedication());
                droneDto.setModel(utils.getStringFromModel(drone.getModel()));
                droneDto.setWeight(drone.getWeight());
                droneDto.setSerialNumber(drone.getSerialNumber());
                droneDto.setId(drone.getId());
                droneDto.setBatteryCapacity(drone.getBatteryCapacity());

                response = new Response("0", "Success", droneDto);

            } else if (medicationOptional.isEmpty() && droneOptional.isPresent()){
                response = new Response("1", "No medication found with this ID " + medicationId);
            } else if (medicationOptional.isPresent() && droneOptional.isEmpty()) {
                response = new Response("1", "No Drone found with this ID " + droneId);
            } else {
                response = new Response("1", "No drone and medication found with both IDs ");
            }

        } catch (Exception e) {
            log.error("Error while saving drone", e);
            response = new Response("1", "Error while saving Drone to the database");
        }

        return response;

    }

    @Override
    public Response checkMedicationOnDrone(Long droneId) {//get Drone

        Response response = null;

        try {

            Optional<Drone> droneOptional = droneRepository.findById(droneId);

            if (droneOptional.isPresent()) {

                Drone drone = droneOptional.get();

                DroneDto droneDto = new DroneDto();
                droneDto.setMedication(drone.getMedication());
                droneDto.setModel(utils.getStringFromModel(drone.getModel()));
                droneDto.setBatteryCapacity(drone.getBatteryCapacity());
                droneDto.setWeight(drone.getWeight());
                droneDto.setState(utils.getStateAsString(drone.getState()));
                droneDto.setSerialNumber(drone.getSerialNumber());

                response = new Response("0", "Success", droneDto);
            } else {
                response = new Response("1", "No Drone found with this ID " + droneId);
            }

        } catch (Exception e) {
            log.error("Error while saving drone", e);
            response = new Response("1", "Error while saving Drone to the database");
        }

        return response;
    }

    @Override
    public Response getDroneBySerialNumber(String serialNumber) {

        Response response = null;

        try {

            Optional<Drone> droneOptional = droneRepository.findBySerialNumber(serialNumber);

            if (droneOptional.isPresent()) {

                Drone drone = droneOptional.get();

                DroneDto droneDto = new DroneDto();
                droneDto.setMedication(drone.getMedication());
                droneDto.setModel(utils.getStringFromModel(drone.getModel()));
                droneDto.setBatteryCapacity(drone.getBatteryCapacity());
                droneDto.setWeight(drone.getWeight());
                droneDto.setState(utils.getStateAsString(drone.getState()));
                droneDto.setSerialNumber(drone.getSerialNumber());

                response = new Response("0", "Success", droneDto);
            } else {
                response = new Response("1", "No Drone found for this serialNumber " + serialNumber);
            }

        } catch (Exception e) {
            log.error("Error while saving drone", e);
            response = new Response("1", "Error while saving Drone to the database");
        }

        return response;

    }

    @Override
    public Response getAllAvailableDrones() {

        Response response = null;

        try {

            List<Drone> dronesList = droneRepository.findIdleDrones(State.IDLE, State.LOADING);

            response = new Response("0", "Success", dronesList);

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;
    }

    @Override
    public Response getAllDrones() {

        Response response = null;

        try {

            List<Drone> dronesList = droneRepository.findAll();

            response = new Response("0", "Success", dronesList);

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;

    }

    @Override
    public Response getDrone(Long droneId) {

        Response response = null;

        try {

            Optional<Drone> optionalDrone = droneRepository.findById(droneId);

            if (optionalDrone.isPresent()){

                Drone drone = optionalDrone.get();

                DroneDto droneDto = new DroneDto();
                droneDto.setId(drone.getId());
                droneDto.setWeight(drone.getWeight());
                droneDto.setModel(utils.getStringFromModel(drone.getModel()));
                droneDto.setMedication(drone.getMedication());
                droneDto.setState(utils.getStateAsString(drone.getState()));
                droneDto.setSerialNumber(drone.getSerialNumber());
                droneDto.setBatteryCapacity(drone.getBatteryCapacity());

                response = new Response("0", "Success", droneDto);

            } else {
                response = new Response("1", "No Drone found with this ID " + droneId);
            }

        } catch (Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;
    }

    @Override
    public Response getBatteryLevel(Long droneId) {

        Response response = null;

        try {
            Optional<Drone> droneOptional = droneRepository.findById(droneId);

            if (droneOptional.isPresent()) {

                Drone drone = droneOptional.get();

                response = new Response("0", "" + drone.getBatteryCapacity());
            } else {

                response = new Response("1", "No Drone found with this ID " + droneId);

            }

        } catch(Exception e) {
            log.error("Failed due to database issue", e);
            response = new Response("1", "Failed due to database issue");
        }

        return response;
    }
}
