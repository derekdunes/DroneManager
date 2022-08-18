package com.musala.drone.manager.Service;

import com.musala.drone.manager.ContainerInitializer;
import com.musala.drone.manager.dto.DroneDto;
import com.musala.drone.manager.dto.MedicationDto;
import com.musala.drone.manager.dto.Response;
import com.musala.drone.manager.enums.Model;
import com.musala.drone.manager.enums.State;
import com.musala.drone.manager.misc.Utils;
import com.musala.drone.manager.services.interfaces.DroneServiceInterface;
import com.musala.drone.manager.services.interfaces.MedicationServiceInterface;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DroneServiceTest extends ContainerInitializer {

    @Autowired
    DroneServiceInterface droneService;

    @Autowired
    MedicationServiceInterface medicationService;

    @Autowired
    Utils utils;

    @Nested
    @DisplayName("All Get Drone Operation Tests")
    public class AllFetchQueries {

        @Test
        @Transactional
        @DisplayName("Get All Created Drones")
        public void getAllDrones() {

            //create drones
            DroneDto drone = new DroneDto();
            drone.setBatteryCapacity(100);
            drone.setModel(utils.getStringFromModel(Model.HEAVYWEIGHT));
            drone.setSerialNumber("Nimbus_2000");
            drone.setWeight(500);

            DroneDto drone2 = new DroneDto();
            drone2.setBatteryCapacity(50);
            drone2.setModel(utils.getStringFromModel(Model.CRUISERWEIGHT));
            drone2.setSerialNumber("Firebolt_2500");
            drone2.setWeight(250);

            DroneDto drone3 = new DroneDto();
            drone3.setBatteryCapacity(600);
            drone3.setModel(utils.getStringFromModel(Model.LIGHTWEIGHT));
            drone3.setSerialNumber("SUV_250");
            drone3.setWeight(50);

            Response response1 = droneService.createDrone(drone);
            Response response2 = droneService.createDrone(drone2);
            Response response3 = droneService.createDrone(drone3);

            assertEquals("0", response1.getStatusCode());
            assertEquals("Success", response1.getStatusMessage());
            assertNotNull(response1.getDrone().getId());

            assertEquals("0", response2.getStatusCode());
            assertEquals("Success", response2.getStatusMessage());
            assertNotNull(response2.getDrone().getId());

            assertEquals("0", response3.getStatusCode());
            assertEquals("Success", response3.getStatusMessage());
            assertNotNull(response3.getDrone().getId());

            Response response = droneService.getAllDrones();

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertEquals(3, response.getDroneList().size());

        }

        //get Drone By Serial Number
        //public Response getDroneBySerialNumber(String serialNumber);
        @ParameterizedTest
        @Transactional
        @DisplayName("Fetch Drone with S/N tests")
        @CsvSource({"100, 50, HEAVYWEIGHT, Croods-419", "75, 150, CRUISERWEIGHT, TOM-319", "60, 500, MIDDLEWEIGHT, Jerry-400", "50, 250, LIGHTWEIGHT, Cat-06"})
        public void getDroneBySerialNumberTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            //droneDto.setState(state);//setting state is useless during creation cause it defaults to idle
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //get saved data
            Response fetchResponse = droneService.getDroneBySerialNumber(serialNumber);

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals("Success", fetchResponse.getStatusMessage());
            assertEquals(weight, Integer.toString(fetchResponse.getDrone().getWeight()));
            assertEquals(batteryCap, String.valueOf(fetchResponse.getDrone().getBatteryCapacity()));
            assertEquals(model, fetchResponse.getDrone().getModel());
            assertEquals(serialNumber, fetchResponse.getDrone().getSerialNumber());
        }

        //public Response getAllAvailableDrones();
        @Test
        @Transactional
        @DisplayName("Get All Idle Drones")
        public void getAllAvailableDrones() {//Only Drones with the status idle

            //create drones and save
            DroneDto drone = new DroneDto();
            drone.setBatteryCapacity(100);
            drone.setModel(utils.getStringFromModel(Model.HEAVYWEIGHT));
            drone.setSerialNumber("Nimbus_2000");
            drone.setWeight(500);

            DroneDto drone2 = new DroneDto();
            drone2.setBatteryCapacity(50);
            drone2.setModel(utils.getStringFromModel(Model.CRUISERWEIGHT));
            drone2.setSerialNumber("Firebolt_2500");
            drone2.setWeight(250);

            DroneDto drone3 = new DroneDto();
            drone3.setBatteryCapacity(600);
            drone3.setModel(utils.getStringFromModel(Model.LIGHTWEIGHT));
            drone3.setSerialNumber("SUV_250");
            drone3.setWeight(50);

            Response response1 = droneService.createDrone(drone);
            Response response2 = droneService.createDrone(drone2);
            Response response3 = droneService.createDrone(drone3);

            assertEquals("0", response1.getStatusCode());
            assertEquals("Success", response1.getStatusMessage());
            assertNotNull(response1.getDrone().getId());

            assertEquals("0", response2.getStatusCode());
            assertEquals("Success", response2.getStatusMessage());
            assertNotNull(response2.getDrone().getId());

            assertEquals("0", response3.getStatusCode());
            assertEquals("Success", response3.getStatusMessage());
            assertNotNull(response3.getDrone().getId());

            //Load 2 drones with medication

            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(200);
            medicationDto.setCode("MSXY-YZ250");
            medicationDto.setName("Medicals");
            medicationDto.setImage("https://www.images.com/drone.png");

            MedicationDto medicationDto2 = new MedicationDto();
            medicationDto2.setWeight(100);
            medicationDto2.setCode("MUSKY-YZ250");
            medicationDto2.setName("Drug Medicals");
            medicationDto2.setImage("https://www.images.com/medic.png");

            //tie 2 ID to the medic object
            Response tiedDrone1Res = droneService.loadDroneWithNewMedication(medicationDto, response1.getDrone().getId());
            Response tiedDrone2Res = droneService.loadDroneWithNewMedication(medicationDto2, response2.getDrone().getId());

            assertEquals("0", tiedDrone1Res.getStatusCode());
            assertEquals("Success", tiedDrone1Res.getStatusMessage());

            assertEquals("0", tiedDrone2Res.getStatusCode());
            assertEquals("Success", tiedDrone2Res.getStatusMessage());

            Response response = droneService.getAllAvailableDrones(); //get all idle drone or unloaded drones

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertEquals(1, response.getDroneList().size());
            assertEquals(response3.getDrone().getId(), response.getDroneList().get(0).getId());
            assertEquals(response3.getDrone().getWeight(), response.getDroneList().get(0).getWeight());
            assertEquals(response3.getDrone().getBatteryCapacity(), response.getDroneList().get(0).getBatteryCapacity());
            assertEquals(response3.getDrone().getModel(), utils.getStringFromModel(response.getDroneList().get(0).getModel()));
            assertEquals(response3.getDrone().getSerialNumber(), response.getDroneList().get(0).getSerialNumber());
            assertEquals(State.IDLE, response.getDroneList().get(0).getState());

        }

        //public Response getDrone(Long droneId);
        @ParameterizedTest
        @Transactional
        @DisplayName("Fetch Drones with Their Ids tests")
        @CsvSource({"100, 50, HEAVYWEIGHT, Croods-419", "75, 150, CRUISERWEIGHT, TOM-319", "60, 500, MIDDLEWEIGHT, Jerry-400", "50, 250, LIGHTWEIGHT, Cat-06"})
        public void getDroneByIdTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //get saved data
            Response fetchResponse = droneService.getDrone(response.getDrone().getId());

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals("Success", fetchResponse.getStatusMessage());
            assertEquals(weight, Integer.toString(fetchResponse.getDrone().getWeight()));
            assertEquals(batteryCap, String.valueOf(fetchResponse.getDrone().getBatteryCapacity()));
            assertEquals(model, fetchResponse.getDrone().getModel());
            assertEquals(serialNumber, fetchResponse.getDrone().getSerialNumber());
        }

        //public Response getBatteryLevel(Long droneId);
        @ParameterizedTest
        @Transactional
        @DisplayName("Fetch Drones Battery Level tests")
        @CsvSource({"100, 50, HEAVYWEIGHT, Croods-419", "75, 150, CRUISERWEIGHT, TOM-319", "60, 500, MIDDLEWEIGHT, Jerry-400", "50, 250, LIGHTWEIGHT, Cat-06"})
        public void getDroneBatteryLevelTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //get saved data
            Response fetchResponse = droneService.getBatteryLevel(response.getDrone().getId());

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals(batteryCap, fetchResponse.getStatusMessage());

        }


    }

    @Nested
    class droneCreationTests {
        //public Response createDrone(DroneDto droneDto);
        @ParameterizedTest
        @Transactional
        @DisplayName("Create Drone tests")//make sure model is valid, serialNumber is Unique and weight is less than 500
        @CsvSource({"100, 50, HEAVYWEIGHT, Sauv-419", "75, 150, CRUISERWEIGHT, Eros-319", "60, 500, MIDDLEWEIGHT, Hommes-400", "50, 250, LIGHTWEIGHT, Creed-06"})
        public void CreateDroneTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            //droneDto.setState(state);//setting state is useless during creation cause it defaults to idle
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //get saved data
            Response fetchResponse = droneService.getDrone(response.getDrone().getId());

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals("Success", fetchResponse.getStatusMessage());
            assertEquals(weight, Integer.toString(fetchResponse.getDrone().getWeight()));
            assertEquals(batteryCap, String.valueOf(fetchResponse.getDrone().getBatteryCapacity()));
            assertEquals(model, fetchResponse.getDrone().getModel());
            assertEquals(serialNumber, fetchResponse.getDrone().getSerialNumber());
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Create Drone with INVALID Weights tests")
        @CsvSource({"100, 2000, HEAVYWEIGHT, MUSK-419", "75, 1500, CRUISERWEIGHT, MOV-319", "60, 550, MIDDLEWEIGHT, SET-400", "50, 2500, LIGHTWEIGHT, LEBR-06"})
        public void CreateDroneWithInvalidWeightsTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("1", response.getStatusCode());
            assertEquals("Weight provided is greater the Maximum acceptable weight(500grm), Please reduce the weight provided", response.getStatusMessage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Create Drone With INVALID Model tests")
        @CsvSource({"100, 50, Heaveight, MUSK-419", "75, 150, ruiserweight, MOV-319", "60, 500, Mileweight, SET-400", "50, 250, invalid, LEBR-06"})
        public void CreateDroneWithInvalidModelsTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            //droneDto.setState(state);//setting state is useless during creation cause it defaults to idle
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            String errorMessage = "INVALID Model type " + droneDto.getModel();
            //validate data
            assertEquals("1", response.getStatusCode());
            assertEquals(errorMessage, response.getStatusMessage());

        }

        //test duplication SerialNumber Saving
        @ParameterizedTest
        @Transactional
        @DisplayName("Create Drone with Duplicate S/N tests")//make sure model is valid, serialNumber is Unique and weight is less than 500
        @CsvSource({"100, 50, HEAVYWEIGHT, MUSK-419", "75, 150, CRUISERWEIGHT, MOV-319", "60, 500, MIDDLEWEIGHT, SET-400", "50, 250, LIGHTWEIGHT, LEBR-06"})
        public void CreateDroneWithDuplicatSerialNumberTests(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            //droneDto.setState(state);//setting state is useless during creation cause it defaults to idle
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            Response duplicateResponse = droneService.createDrone(droneDto);

            //validate data
            assertEquals("1", duplicateResponse.getStatusCode());
            assertEquals("Error while saving Drone to the database", duplicateResponse.getStatusMessage());

        }

    }

    @Nested
    @DisplayName("All update Drone Tests")
    class UpdateDroneTests {
        //check for invalid models also
        //check for weights above 500

        //update Drone
        //public Response updateDrone(DroneDto droneDto, Long droneId);
        @ParameterizedTest
        @Transactional
        @DisplayName("update Drone tests")//make sure model is valid, serialNumber is Unique and weight is less than 500
        @CsvSource({"100, 50, HEAVYWEIGHT, Sauv-419, 75, 150, CRUISERWEIGHT, Eros-319", "60, 500, MIDDLEWEIGHT, Hommes-400, 50, 250, LIGHTWEIGHT, Creed-06"})
        public void updateDroneTests(String batteryCap, String weight, String model, String serialNumber, String newBatteryCap, String newWeight, String newModel, String newSerialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //Create Drone
            DroneDto droneUpdate = new DroneDto();
            droneUpdate.setBatteryCapacity(Integer.parseInt(newBatteryCap));
            droneUpdate.setWeight(Integer.parseInt(newWeight));
            droneUpdate.setModel(newModel);
            droneUpdate.setSerialNumber(newSerialNumber);

            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //update drone
            Response updateResponse = droneService.updateDrone(droneUpdate, response.getDrone().getId());

            assertEquals("0", updateResponse.getStatusCode());
            assertEquals("Success", updateResponse.getStatusMessage());

            Response fetchResponse = droneService.getDrone(response.getDrone().getId());

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals("Success", fetchResponse.getStatusMessage());
            assertEquals(newWeight, Integer.toString(fetchResponse.getDrone().getWeight()));
            assertEquals(newBatteryCap, String.valueOf(fetchResponse.getDrone().getBatteryCapacity()));
            assertEquals(newModel, fetchResponse.getDrone().getModel());
            assertEquals(newSerialNumber, fetchResponse.getDrone().getSerialNumber());
        }

        @ParameterizedTest
        @Transactional
        @DisplayName("update Drone with INVALID Weights tests")
        @CsvSource({"100, 50, HEAVYWEIGHT, Sauv-419, 75, 1500, CRUISERWEIGHT, Eros-319", "60, 500, MIDDLEWEIGHT, Hommes-400, 50, 2500, LIGHTWEIGHT, Creed-06"})
        public void updateDroneWithInvalidWeightsTests(String batteryCap, String weight, String model, String serialNumber, String newBatteryCap, String newWeight, String newModel, String newSerialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //Create Drone
            DroneDto droneUpdate = new DroneDto();
            droneUpdate.setBatteryCapacity(Integer.parseInt(newBatteryCap));
            droneUpdate.setWeight(Integer.parseInt(newWeight));
            droneUpdate.setModel(newModel);
            droneUpdate.setSerialNumber(newSerialNumber);

            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //update drone
            Response updateResponse = droneService.updateDrone(droneUpdate, response.getDrone().getId());

            assertEquals("1", updateResponse.getStatusCode());
            assertEquals("Weight provided is greater the Maximum acceptable weight(500grm), Please reduce the weight provided", updateResponse.getStatusMessage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("update Drone With INVALID Model tests")
        @CsvSource({"100, 50, HEAVYWEIGHT, MUSK-419, 75, 150, ruiserweight, MOV-319", "60, 500, MIDDLEWEIGHT, SET-400, 50, 250, invalid, LEBR-06"})
        public void DroneWithInvalidModelsTests(String batteryCap, String weight, String model, String serialNumber, String newBatteryCap, String newWeight, String newModel, String newSerialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //Create Drone
            DroneDto droneUpdate = new DroneDto();
            droneUpdate.setBatteryCapacity(Integer.parseInt(newBatteryCap));
            droneUpdate.setWeight(Integer.parseInt(newWeight));
            droneUpdate.setModel(newModel);
            droneUpdate.setSerialNumber(newSerialNumber);

            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //update drone
            Response updateResponse = droneService.updateDrone(droneUpdate, response.getDrone().getId());

            String errorMessage = "INVALID Model type " + droneUpdate.getModel();

            assertEquals("1", updateResponse.getStatusCode());
            assertEquals(errorMessage, updateResponse.getStatusMessage());

        }

        //getDroneStatus
        //public Response updateDroneStatusByOneStep(Long droneId);
        @ParameterizedTest
        @Transactional
        @DisplayName("Update Drone Status Sequencially")
        @CsvSource({"100, 50, HEAVYWEIGHT, Croods-419", "75, 150, CRUISERWEIGHT, TOM-319"})
        public void updateDroneStatusSerially(String batteryCap, String weight, String model, String serialNumber) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //save
            Response response = droneService.createDrone(droneDto); //state is set to idle by default

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //get saved data
            Response response1 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response1.getStatusCode());
            assertEquals("Success", response1.getStatusMessage());
            assertEquals(utils.getStateAsString(State.LOADING), response1.getDrone().getState());

            Response response2 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response2.getStatusCode());
            assertEquals("Success", response2.getStatusMessage());
            assertEquals(utils.getStateAsString(State.LOADED), response2.getDrone().getState());

            Response response3 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response3.getStatusCode());
            assertEquals("Success", response3.getStatusMessage());
            assertEquals(utils.getStateAsString(State.DELIVERING), response3.getDrone().getState());

            Response response4 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response4.getStatusCode());
            assertEquals("Success", response4.getStatusMessage());
            assertEquals(utils.getStateAsString(State.DELIVERED), response4.getDrone().getState());

            Response response5 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response5.getStatusCode());
            assertEquals("Success", response5.getStatusMessage());
            assertEquals(utils.getStateAsString(State.RETURNING), response5.getDrone().getState());

            Response fetchResponse = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals("Success", fetchResponse.getStatusMessage());
            assertEquals(utils.getStateAsString(State.IDLE), fetchResponse.getDrone().getState());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Update Drone Status Sequencially and disJoin Delivered Medication")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 50, MUSK-419, twitter, dummyLink", "75, 250, CRUISERWEIGHT, TOM-319, 150, AMG-419, Bird, nbaLink"})
        public void updateDroneStatusSeriallyAndRemoveDeliveredMedic(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {
            //test invalid model scenarios
            //test invalid weights

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            //save
            Response response = droneService.createDrone(droneDto); //state is set to idle by default

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            //get saved data
            Response response1 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response1.getStatusCode());
            assertEquals("Success", response1.getStatusMessage());
            assertEquals(utils.getStateAsString(State.LOADING), response1.getDrone().getState());

            //append medication to drone
            Response tieResponse = droneService.loadDroneWithNewMedication(medicationDto, response.getDrone().getId());

            assertEquals("0", tieResponse.getStatusCode());
            assertEquals("Success", tieResponse.getStatusMessage());
            assertEquals(utils.getStateAsString(State.LOADED), tieResponse.getDrone().getState());

            Response response3 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response3.getStatusCode());
            assertEquals("Success", response3.getStatusMessage());
            assertEquals(utils.getStateAsString(State.DELIVERING), response3.getDrone().getState());

            Response response4 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response4.getStatusCode());
            assertEquals("Success", response4.getStatusMessage());
            assertEquals(utils.getStateAsString(State.DELIVERED), response4.getDrone().getState());
            assertNull(response4.getDrone().getMedication()); //confirm that medication have been removed/untied from drone// delivered

            Response response5 = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", response5.getStatusCode());
            assertEquals("Success", response5.getStatusMessage());
            assertEquals(utils.getStateAsString(State.RETURNING), response5.getDrone().getState());

            Response fetchResponse = droneService.updateDroneStatusByOneStep(response.getDrone().getId());

            //validate data
            assertEquals("0", fetchResponse.getStatusCode());
            assertEquals("Success", fetchResponse.getStatusMessage());
            assertEquals(utils.getStateAsString(State.IDLE), fetchResponse.getDrone().getState());

        }

    }

    //delete Drone
    //public Response deleteDrone(Long droneId);
    //delete tied and untied drones
    @Nested
    @DisplayName("All delete Drone Tests")
    class deleteDroneTests {

        @ParameterizedTest
        @DisplayName("Delete Untied Medications tests")
        @CsvSource({"100, 50, HEAVYWEIGHT, Sauv-419", "75, 150, CRUISERWEIGHT, Eros-319", "60, 500, MIDDLEWEIGHT, Hommes-400", "50, 250, LIGHTWEIGHT, Creed-06"})
        public void deleteUntiedDroneTests(String batteryCap, String weight, String model, String serialNumber) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            //droneDto.setState(state);//setting state is useless during creation cause it defaults to idle
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);


            //save
            Response response = droneService.createDrone(droneDto);

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNotNull(response.getDrone().getId());

            Response deleteResponse = droneService. deleteDrone(response.getDrone().getId());

            assertEquals("0", deleteResponse.getStatusCode());
            assertEquals("Success", deleteResponse.getStatusMessage());

            //get saved data
            Response fetchResponse = droneService.getDrone(response.getDrone().getId());

            String errorMessage = "No Drone found with this ID " + response.getDrone().getId();

            //validate data
            assertEquals("1", fetchResponse.getStatusCode());
            assertEquals(errorMessage, fetchResponse.getStatusMessage());

        }

        //public DroneResponse checkDroneCarryingMedication(Long medicationId);
        //create Drone, //create medic tie medic to drone and vice versa
        //perform query
        @Test
        @Transactional
        @DisplayName("Delete Medication Tied to a Drone test")
        public void deleteTiedMedicationTests(){

            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(100);
            droneDto.setModel("HEAVYWEIGHT");
            droneDto.setSerialNumber("Tesla_3");
            droneDto.setWeight(500);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //create medic and tie it to drone
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(100);
            medicationDto.setCode("MSX-YZ250");
            medicationDto.setName("Drug Medicals");
            medicationDto.setImage("https://www.images.com/drone.png");

            //Tie medication to drone
            Response response = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());

            //try to delete tied medication to drone
            Response deleteResponse = droneService.deleteDrone(droneResponse.getDrone().getId());

            String errorMessage = "Cannot delete Id, still carrying a medication with Id " + response.getDrone().getMedication().getId();

            //validate delete
            assertEquals("1", deleteResponse.getStatusCode());
            assertEquals(errorMessage, deleteResponse.getStatusMessage());

        }

        @Test
        @Transactional
        @DisplayName("Delete INVALID Drone Id test")
        public void deleteNonExistingIdTests(){


            //try to delete tied medication to drone
            Response deleteResponse = droneService.deleteDrone(1L);

            String errorMessage = "No Drone found with this ID " + 1L;

            //validate delete
            assertEquals("1", deleteResponse.getStatusCode());
            assertEquals(errorMessage, deleteResponse.getStatusMessage());

        }

    }

    @Nested
    @DisplayName("All Load Drone Tests")
    class loadDroneTests {

        @ParameterizedTest
        @Transactional
        @DisplayName("Load Non-Existing Drone with New Medication tests")
        @CsvSource({"50, MUSK-419, twitter, dummyLink", "150, AMG-419, Bird, nbaLink"})
        public void loadInvalidDroneWithNewMedicationTest(String medicWeight, String code, String name, String image) {

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            //Tie medication to drone
            Response response = droneService.loadDroneWithNewMedication(medicationDto, 1L);

            String errorMessage = "No Drone found with this ID " + 1L;

            assertEquals("1", response.getStatusCode());
            assertEquals(errorMessage, response.getStatusMessage());

        }

        //public Response LoadDroneWithNewMedication(MedicationDto medicationdto, Long droneId);
        @ParameterizedTest
        @Transactional
        @DisplayName("Load Drone with New Medication tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 50, MUSK-419, twitter, dummyLink", "75, 250, CRUISERWEIGHT, TOM-319, 150, AMG-419, Bird, nbaLink"})
        public void loadDroneWithNewMedicationTest(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            //assertNotNull(response.getMedication().getId());

            assertEquals(Integer.parseInt(batteryCap),response.getDrone().getBatteryCapacity());
            assertEquals(Integer.parseInt(weight), response.getDrone().getWeight());
            assertEquals(model, response.getDrone().getModel());
            assertEquals(serialNumber, response.getDrone().getSerialNumber());
            assertEquals(utils.getStateAsString(State.LOADED), response.getDrone().getState());

            assertEquals(Integer.parseInt(medicWeight), response.getMedication().getWeight());
            assertEquals(code, response.getMedication().getCode());
            assertEquals(name, response.getMedication().getName());
            assertEquals(image, response.getMedication().getImage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Load Occupied Drone with New Medic tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 50, MUSK-419, twitter, dummyLink", "75, 250, CRUISERWEIGHT, TOM-319, 150, AMG-419, Bird, nbaLink"})
        public void loadDroneWithNewMedicationTwiceTest(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());

            Response failureResponse = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());

            assertEquals("1", failureResponse.getStatusCode());
            assertEquals("Drone cannot be loaded cause its not Idle", failureResponse.getStatusMessage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Load Drone with New Heavier Medic tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 550, MUSK-419, twitter, dummyLink", "75, 250, CRUISERWEIGHT, TOM-319, 450, AMG-419, Bird, nbaLink"})
        public void loadDroneWithNewHeavierMedicTest(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());

            assertEquals("1", response.getStatusCode());
            assertEquals("Medication is heavier than the Drone, please pick another drone", response.getStatusMessage());

        }

        //public Response LoadDroneWithExistingMedication(Long medicationId, Long droneId);
        @ParameterizedTest
        @Transactional
        @DisplayName("Load Drone with Existing Medication tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 50, MUSK-419, twitter, dummyLink", "75, 250, CRUISERWEIGHT, TOM-319, 150, AMG-419, Bird, nbaLink"})
        public void loadDroneWithExistingMedicationTest(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response medicationResponse = medicationService.createMedication(medicationDto);

            assertEquals("0", medicationResponse.getStatusCode());
            assertEquals("Success", medicationResponse.getStatusMessage());
            assertNotNull(medicationResponse.getMedication().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithExistingMedication(medicationResponse.getMedication().getId(), droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());

            assertEquals(Integer.parseInt(batteryCap),response.getDrone().getBatteryCapacity());
            assertEquals(Integer.parseInt(weight), response.getDrone().getWeight());
            assertEquals(model, response.getDrone().getModel());
            assertEquals(serialNumber, response.getDrone().getSerialNumber());
            assertEquals(utils.getStateAsString(State.LOADED), response.getDrone().getState());

            assertEquals(Integer.parseInt(medicWeight), response.getDrone().getMedication().getWeight());
            assertEquals(code, response.getDrone().getMedication().getCode());
            assertEquals(name, response.getDrone().getMedication().getName());
            assertEquals(image, response.getDrone().getMedication().getImage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Load Occupied Drone with Existing Medication tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 50, MUSK-419, twitter, dummyLink"})
        public void loadDroneWithExistingMedicationTwiceTest(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response medicationResponse = medicationService.createMedication(medicationDto);

            assertEquals("0", medicationResponse.getStatusCode());
            assertEquals("Success", medicationResponse.getStatusMessage());
            assertNotNull(medicationResponse.getMedication().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithExistingMedication(medicationResponse.getMedication().getId(), droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());

            Response failureResponse = droneService.loadDroneWithExistingMedication(medicationResponse.getMedication().getId(), droneResponse.getDrone().getId());

            assertEquals("1", failureResponse.getStatusCode());
            assertEquals("Drone cannot be loaded cause its not Idle", failureResponse.getStatusMessage());

        }

        //test adding a medication bigger than the drone size
        @ParameterizedTest
        @Transactional
        @DisplayName("Load Drone with Existing Heavier Medication tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 550, MUSK-419, twitter, dummyLink"})
        public void loadDroneWithExistingHeavierMedicTest(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response medicationResponse = medicationService.createMedication(medicationDto);

            assertEquals("0", medicationResponse.getStatusCode());
            assertEquals("Success", medicationResponse.getStatusMessage());
            assertNotNull(medicationResponse.getMedication().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithExistingMedication(medicationResponse.getMedication().getId(), droneResponse.getDrone().getId());

            assertEquals("1", response.getStatusCode());
            assertEquals("Medication is heavier than the Drone, please pick another drone", response.getStatusMessage());

        }

        @Test
        @Transactional
        @DisplayName("Load Method INVALID Drone and Medic Id tests")
        public void loadDroneWithInvalidId() {

            //Tie medication to drone
            Response response = droneService.loadDroneWithExistingMedication(1L, 1L);

            assertEquals("1", response.getStatusCode());
            assertEquals("No drone and medication found with both IDs ", response.getStatusMessage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Load INVALID Drone with Valid Medication tests")
        @CsvSource({"500, MUSK-419, twitter, dummyLink"})
        public void loadValidMedicOnInvalidDroneTest(String medicWeight, String code, String name, String image) {

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response medicationResponse = medicationService.createMedication(medicationDto);

            assertEquals("0", medicationResponse.getStatusCode());
            assertEquals("Success", medicationResponse.getStatusMessage());
            assertNotNull(medicationResponse.getMedication().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithExistingMedication(medicationResponse.getMedication().getId(), 1L);

            String errorMessage = "No Drone found with this ID " + 1L;

            assertEquals("1", response.getStatusCode());
            assertEquals(errorMessage, response.getStatusMessage());

        }

        @ParameterizedTest
        @Transactional
        @DisplayName("Load Valid Drone with INVALID Medication tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419"})
        public void loadInvalidMedicOnValidDroneTest(String batteryCap, String weight, String model, String serialNumber) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithExistingMedication(1L, droneResponse.getDrone().getId());

            String errorMessage = "No medication found with this ID " + 1L;

            assertEquals("1", response.getStatusCode());
            assertEquals(errorMessage, response.getStatusMessage());

        }

    }

    //public Response checkMedicationOnDrone(Long droneId);
    @Nested
    class checkMedicationOnDroneTests {

        //case valid check of a Drone with non-existing Medication
        @ParameterizedTest
        @Transactional
        @DisplayName("Check unloaded valid Drone for Medication")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419"})
        public void loadInvalidMedicOnValidDroneTest(String batteryCap, String weight, String model, String serialNumber) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //Tie medication to drone
            Response response = droneService.checkMedicationOnDrone(droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertNull(response.getDrone().getMedication());

        }

        //case valid check of a Drone with existing Medication
        @ParameterizedTest
        @Transactional
        @DisplayName("Check Valid Drone with tied Medication tests")
        @CsvSource({"100, 500, HEAVYWEIGHT, Croods-419, 50, MUSK-419, twitter, dummyLink", "75, 250, CRUISERWEIGHT, TOM-319, 150, AMG-419, Bird, nbaLink"})
        public void checkValidDroneForMedicationTests(String batteryCap, String weight, String model, String serialNumber, String medicWeight, String code, String name, String image) {

            //Create Drone
            DroneDto droneDto = new DroneDto();
            droneDto.setBatteryCapacity(Integer.parseInt(batteryCap));
            droneDto.setWeight(Integer.parseInt(weight));
            droneDto.setModel(model);
            droneDto.setSerialNumber(serialNumber);

            //create Medic to tie to drone after drone creation
            MedicationDto medicationDto = new MedicationDto();
            medicationDto.setWeight(Integer.parseInt(medicWeight));
            medicationDto.setCode(code);
            medicationDto.setName(name);
            medicationDto.setImage(image);

            Response droneResponse = droneService.createDrone(droneDto);

            assertEquals("0", droneResponse.getStatusCode());
            assertEquals("Success", droneResponse.getStatusMessage());
            assertNotNull(droneResponse.getDrone().getId());

            //Tie medication to drone
            Response response = droneService.loadDroneWithNewMedication(medicationDto, droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());

            Response checkResponse = droneService.checkMedicationOnDrone(droneResponse.getDrone().getId());

            assertEquals("0", response.getStatusCode());
            assertEquals("Success", response.getStatusMessage());
            assertEquals(Integer.parseInt(medicWeight), checkResponse.getDrone().getMedication().getWeight());
            assertEquals(code,checkResponse.getDrone().getMedication().getCode());
            assertEquals(name, checkResponse.getDrone().getMedication().getName());
            assertEquals(image, checkResponse.getDrone().getMedication().getImage());
            //assertEquals(response.getMedication().getId(), checkResponse.getDrone().getMedication().getId());

        }

        //case of invalid drone id
        @Test
        @Transactional
        @DisplayName("Check INVALID Drone for Medication")
        public void checkInvalidDroneForMedication() {

            //Tie medication to drone
            Response response = droneService.checkMedicationOnDrone(1L);

            String errorMessage = "No Drone found with this ID " + 1L;

            assertEquals("1", response.getStatusCode());
            assertEquals(errorMessage, response.getStatusMessage());

        }

    }

}
